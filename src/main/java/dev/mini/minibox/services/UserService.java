package dev.mini.minibox.services;

import dev.mini.minibox.entities.EmailTokenEntity;
import dev.mini.minibox.entities.UserEntity;
import dev.mini.minibox.exceptions.TransactionalException;
import dev.mini.minibox.mappers.EmailTokenMapper;
import dev.mini.minibox.mappers.UserMapper;
import dev.mini.minibox.results.CommonResult;
import dev.mini.minibox.results.Result;
import dev.mini.minibox.results.user.LoginResult;
import dev.mini.minibox.results.user.RegisterResult;
import dev.mini.minibox.results.user.ResolveRecoverPasswordResult;
import dev.mini.minibox.results.user.ValidateEmailTokenResult;
import dev.mini.minibox.utils.CryptoUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static java.time.LocalDateTime.now;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final EmailTokenMapper emailTokenMapper;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public UserService(UserMapper userMapper, EmailTokenMapper emailTokenMapper, JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.userMapper = userMapper;
        this.emailTokenMapper = emailTokenMapper;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // 로그인
    // ( 이메일로 유저를 찾고 비밀번호를 비교하고 인증 및 정지 여부를 확인하여 결과를 반환 )
    // 회원 로그인을 위한 메서드. 문자열 이메일(email)과 문자열 비밀번호(password)를 전달 받아,
    // 이메일과 일치하는 UserEntity를 데이터베이스에서 선택(SELECT)하고,
    // 선택된 UserEntity가 가지는 비밀번호(password) 멤버를 전달 받은 비밀번호와 비교, 로그인 결과를 반환.
    public Result login(UserEntity user) {
        if (user == null ||
            user.getEmail() == null || user.getEmail().length() < 8 || user.getEmail().length() > 50 ||
            user.getPassword() == null || user.getPassword().length() < 6 || user.getPassword().length() > 50) {
            return CommonResult.FAILURE;
        }
        UserEntity dbUser = this.userMapper.selectUserByEmail(user.getEmail());
        if (dbUser == null) {
            return CommonResult.FAILURE;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return CommonResult.FAILURE;
        }
        if (dbUser.getDeletedAt() != null) {
            return LoginResult.FAILURE_DELETED;
        }
        if (!dbUser.isVerified()) {
            return LoginResult.FAILURE_NOT_VERIFIED;
        }
        if (dbUser.isSuspended()) {
            return LoginResult.FAILURE_SUSPENDED;
        }
        user.setPassword(dbUser.getPassword());
        user.setNickname(dbUser.getNickname());
        user.setContact(dbUser.getContact());
        user.setCreatedAt(dbUser.getCreatedAt());
        user.setDeletedAt(dbUser.getDeletedAt());
        user.setAdmin(dbUser.isAdmin());
        user.setSuspended(dbUser.isSuspended());
        user.setVerified(dbUser.isVerified());
        return CommonResult.SUCCESS;
    }

    // 비밀번호 재설정
    // 이메일로 유저를 찾고 이메일토큰을 발급 이메일로 발송, 성공 실패 결과를 반환
    @Transactional
    public Result provokeRecoverPassword(HttpServletRequest request, String email) throws MessagingException {
        if (email == null || email.length() < 8 || email.length() > 50) {
            return CommonResult.FAILURE;
        }
        UserEntity user = this.userMapper.selectUserByEmail(email);
        if (user == null || user.getDeletedAt() != null) {
            return CommonResult.FAILURE;
        }
        EmailTokenEntity emailToken = new EmailTokenEntity();
        emailToken.setUserEmail(user.getEmail());
        emailToken.setKey(CryptoUtils.hashSha512(String.format("%s%s%f%f",
                user.getEmail(),
                user.getPassword(),
                Math.random(),
                Math.random())));
        emailToken.setCreatedAt(now());
        emailToken.setExpiresAt(now().plusHours(24));
        emailToken.setUsed(false);
        if (this.emailTokenMapper.insertEmailToken(emailToken) == 0) {
            throw new TransactionalException();
        }
        String validationLink = String.format("%s://%s:%d/user/recover-password?userEmail=%s&key=%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                emailToken.getUserEmail(),
                emailToken.getKey());
        Context context = new Context();
        context.setVariable("validationLink", validationLink);
        String mailText = this.templateEngine.process("email/recoverPassword", context);
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("admin@mega.com");
        mimeMessageHelper.setTo(emailToken.getUserEmail());
        mimeMessageHelper.setSubject("[미니박스] 비밀번호 재설정 인증 링크");
        mimeMessageHelper.setText(mailText, true);
        this.mailSender.send(mimeMessage);
        return CommonResult.SUCCESS;
    }

    // 아이디(이메일) 찾기
    // 연락처와 일치하는 유저를 찾아 이메일을 돌려주고, 성공 실패 결과를 반환
    public Result recoverEmail(UserEntity user) {
        if (user == null ||
            user.getContact() == null || user.getContact().length() < 10 || user.getContact().length() > 12) {
            return CommonResult.FAILURE;
        }
        UserEntity dbUser = this.userMapper.selectUserByContact(user.getContact());
        if (dbUser == null || dbUser.getDeletedAt() != null) {
            return CommonResult.FAILURE;
        }
        user.setEmail(dbUser.getEmail());
        return CommonResult.SUCCESS;
    }

    // 회원가입
    // 입력받은 회원정보를 데이터에 저장하고 이메일토큰을 발급 이메일로 발송, 성공 실패 결과를 반환
    @Transactional
    public Result register(HttpServletRequest request, UserEntity user) throws TransactionException, MessagingException {
        if (user == null ||
            user.getEmail() == null || user.getEmail().length() < 8 || user.getEmail().length() > 50 ||
            user.getPassword() == null || user.getPassword().length() < 6 || user.getPassword().length() > 50 ||
            user.getNickname() == null || user.getNickname().length() < 2 || user.getNickname().length() > 10 ||
            user.getContact() == null || user.getContact().length() < 10 || user.getContact().length() > 12) {
            return CommonResult.FAILURE;
        }
        if (this.userMapper.selectUserByEmail(user.getEmail()) != null) {
            return RegisterResult.FAILURE_DUPLICATE_EMAIL;
        }
        if (this.userMapper.selectUserByContact(user.getContact()) != null) {
            return RegisterResult.FAILURE_DUPLICATE_CONTACT;
        }
        if (this.userMapper.selectUserByNickname(user.getNickname()) != null) {
            return RegisterResult.FAILURE_DUPLICATE_NICKNAME;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(now());
        user.setUpdatedAt(now());
        user.setDeletedAt(null);
        user.setAdmin(false);
        user.setSuspended(false);
        user.setVerified(false);
        if (this.userMapper.insertUser(user) == 0) {
            throw new TransactionalException();
        }
        EmailTokenEntity emailToken = new EmailTokenEntity();
        emailToken.setUserEmail(user.getEmail());
        emailToken.setKey(CryptoUtils.hashSha512(String.format("%s%s%f%f",
                user.getEmail(),
                user.getPassword(),
                Math.random(),
                Math.random())));
        emailToken.setCreatedAt(now());
        emailToken.setExpiresAt(now().plusHours(24));
        emailToken.setUsed(false);
        if (this.emailTokenMapper.insertEmailToken(emailToken) == 0) {
            throw new TransactionalException();
        }
        String validationLink = String.format("%s://%s:%d/user/validate-email-token?userEmail=%s&key=%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                emailToken.getUserEmail(),
                emailToken.getKey());
        Context context = new Context();
        context.setVariable("validationLink", validationLink);
        String mailText = this.templateEngine.process("email/register", context);
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("haechi1826@gmail.com");
        mimeMessageHelper.setTo(emailToken.getUserEmail());
        mimeMessageHelper.setSubject("[미니박스] 회원가입 인증 링크");
        mimeMessageHelper.setText(mailText, true);
        this.mailSender.send(mimeMessage);
        return CommonResult.SUCCESS;
    }

    // 비밀번호 변경 수정
    // 이메일과 키로 이메일토큰을 찾아 비교하여 토큰을 사용처리하고 이메일로 유저를 찾아 비밀번호를 업데이트, 성공 실패 결과를 반환
    @Transactional
    public Result resolveRecoverPassword(EmailTokenEntity emailToken, String password) {
        if (emailToken == null ||
            emailToken.getUserEmail() == null || emailToken.getUserEmail().length() < 8 || emailToken.getUserEmail().length() > 50 ||
            emailToken.getKey() == null || emailToken.getKey().length() != 128 ||
            password == null || password.length() < 6 || password.length() > 50) {
            return CommonResult.FAILURE;
        }
        EmailTokenEntity dbEmailToken = this.emailTokenMapper.selectEmailTokenByUserEmailAndKey(emailToken.getUserEmail(), emailToken.getKey());
        if (dbEmailToken == null || dbEmailToken.isUsed()) {
            return CommonResult.FAILURE;
        }
        if (dbEmailToken.getExpiresAt().isBefore(now())) {
            return ResolveRecoverPasswordResult.FAILURE_EXPIRED;
        }
        dbEmailToken.setUsed(true);
        if (this.emailTokenMapper.updateEmailToken(dbEmailToken) == 0) {
            throw new TransactionalException();
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserEntity user = this.userMapper.selectUserByEmail(emailToken.getUserEmail());
        user.setPassword(encoder.encode(password));
        if (this.userMapper.updateUser(user) == 0) {
            throw new TransactionalException();
        }
        return CommonResult.SUCCESS;
    }

    // 회원가입 이메일 인증시 이메일토큰을 사용처리하고 유저에서 인증처리, 성공 실패 결과를 반환
    @Transactional
    public Result validateEmailToken(EmailTokenEntity emailToken) {
        if (emailToken == null ||
            emailToken.getUserEmail() == null || emailToken.getUserEmail().length() < 8 || emailToken.getUserEmail().length() > 50 ||
            emailToken.getKey() == null || emailToken.getKey().length() != 128) {
            return CommonResult.FAILURE;
        }
        EmailTokenEntity dbEmailToken = this.emailTokenMapper.selectEmailTokenByUserEmailAndKey(emailToken.getUserEmail(), emailToken.getKey());
        if (dbEmailToken == null || dbEmailToken.isUsed()) {
            return CommonResult.FAILURE;
        }
        if (dbEmailToken.getExpiresAt().isBefore(now())) {
            return ValidateEmailTokenResult.FAILURE_EXPIRED;
        }
        dbEmailToken.setUsed(true);
        if (this.emailTokenMapper.updateEmailToken(dbEmailToken) == 0) {
            throw new TransactionalException();
        }
        UserEntity user = this.userMapper.selectUserByEmail(emailToken.getUserEmail());
        user.setVerified(true);
        if (this.userMapper.updateUser(user) == 0) {
            throw new TransactionalException();
        }
        return CommonResult.SUCCESS;
    }

    // 유저 이메일로 유저 찾기
    public UserEntity getUser(String userEmail) {
        return this.userMapper.selectUserByEmail(userEmail);
    }

    // 회원 정보 수정
    // dbUser를 만들어 비밀번호가 일치하는지 확인하고, 연락처와 이메일이 중복된 것이 있는지 확인
    // 입력한 정보로 회원정보 수정
    public Result resolveModifyUser(String userEmail, UserEntity userEntity) {
        UserEntity dbUser = this.userMapper.selectUserByEmail(userEmail);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(userEntity.getPassword(), dbUser.getPassword())) {
            return RegisterResult.FAILURE_PASSWORD;
        }
        if (!userEntity.getContact().matches(dbUser.getContact())) {
            if (this.userMapper.selectUserByContact(userEntity.getContact()) != null) {
                return RegisterResult.FAILURE_DUPLICATE_CONTACT;
            }
        }
        if (!userEntity.getNickname().matches(dbUser.getNickname())) {
            if (this.userMapper.selectUserByNickname(userEntity.getNickname()) != null) {
                return RegisterResult.FAILURE_DUPLICATE_NICKNAME;
            }
        }
        dbUser.setContact(userEntity.getContact());
        dbUser.setNickname(userEntity.getNickname());
        dbUser.setBirthdate(userEntity.getBirthdate());
        dbUser.setUpdatedAt(now());
        this.userMapper.updateUser(dbUser);
        return CommonResult.SUCCESS;
    }

    // 회원탈퇴
    // deletedAt 수정하여 데이터는 남기고 탈퇴처리
    public Result deleteCancelUser(String userEmail) {
        UserEntity dbUser = this.userMapper.selectUserByEmail(userEmail);
        dbUser.setDeletedAt(now());
        this.userMapper.updateUser(dbUser);
        return CommonResult.SUCCESS;
    }

    // 마이페이지에서 비밀번호 변경
    // 비밀번호가 db와 일치하는지 확인 후,
    // 새로운 비밀번호를 인코딩하여 설정
    public Result changePassword(String userEmail, String beforePassword, String newPassword) {
        UserEntity dbUser = this.userMapper.selectUserByEmail(userEmail);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(beforePassword, dbUser.getPassword())) {
            return RegisterResult.FAILURE_PASSWORD;
        }
        dbUser.setPassword(encoder.encode(newPassword));
        dbUser.setUpdatedAt(now());
        this.userMapper.updateUser(dbUser);
        return CommonResult.SUCCESS;
    }
}
