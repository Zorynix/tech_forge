package com.techmarket.authservice.adapter.out;

import com.techmarket.authservice.application.port.out.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SmtpEmailSender implements EmailSender {

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailSender.class);

    private final JavaMailSender mailSender;

    public SmtpEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("⛩️ Tech Forge — Код подтверждения");
            helper.setText(buildHtml(code), true);
            mailSender.send(message);
            log.info("Verification email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}", to, e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    private String buildHtml(String code) {
        String[] digits = code.split("");
        StringBuilder codeBoxes = new StringBuilder();
        for (String d : digits) {
            codeBoxes.append(
                "<div style=\"display:inline-block;width:48px;height:56px;margin:0 5px;" +
                "background:linear-gradient(135deg,#1a1a2e 0%,#16213e 100%);" +
                "border:2px solid #e94560;border-radius:10px;" +
                "font-size:28px;font-weight:700;color:#ffffff;" +
                "line-height:56px;text-align:center;" +
                "box-shadow:0 4px 15px rgba(233,69,96,0.3);\">")
                .append(d)
                .append("</div>");
        }

        return "<!DOCTYPE html>" +
            "<html lang=\"ru\">" +
            "<head><meta charset=\"UTF-8\"></head>" +
            "<body style=\"margin:0;padding:0;background:#0f0f1a;font-family:'Segoe UI',Arial,sans-serif;\">" +
            "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#0f0f1a;padding:40px 0;\">" +
            "<tr><td align=\"center\">" +

            "<table width=\"520\" cellpadding=\"0\" cellspacing=\"0\" style=\"" +
                "background:linear-gradient(180deg,#1a1a2e 0%,#16213e 100%);" +
                "border-radius:20px;" +
                "border:1px solid rgba(233,69,96,0.3);" +
                "box-shadow:0 20px 60px rgba(0,0,0,0.5),0 0 40px rgba(233,69,96,0.1);" +
                "overflow:hidden;\">" +

            "<!-- Header with Torii gate pattern -->" +
            "<tr><td style=\"padding:0;\">" +
                "<div style=\"background:linear-gradient(135deg,#e94560 0%,#c23152 50%,#e94560 100%);" +
                    "padding:30px 40px;text-align:center;position:relative;\">" +

                    "<!-- Decorative top border -->" +
                    "<div style=\"font-size:24px;letter-spacing:12px;margin-bottom:8px;\">⛩️ 🌸 ⛩️</div>" +

                    "<h1 style=\"margin:0;font-size:26px;font-weight:700;" +
                        "color:#ffffff;letter-spacing:3px;" +
                        "text-shadow:0 2px 10px rgba(0,0,0,0.3);\">" +
                        "Tech Forge" +
                    "</h1>" +

                    "<div style=\"margin-top:6px;font-size:11px;color:rgba(255,255,255,0.8);" +
                        "letter-spacing:6px;text-transform:uppercase;\">テックフォージ</div>" +

                "</div>" +
            "</td></tr>" +

            "<!-- Body -->" +
            "<tr><td style=\"padding:40px 40px 20px;\">" +

                "<!-- Greeting -->" +
                "<p style=\"margin:0 0 6px;font-size:13px;color:#e94560;" +
                    "letter-spacing:4px;text-transform:uppercase;\">認証コード</p>" +
                "<p style=\"margin:0 0 20px;font-size:18px;color:#e0e0e0;line-height:1.5;\">" +
                    "Ваш код подтверждения:" +
                "</p>" +

                "<!-- Code block -->" +
                "<div style=\"text-align:center;margin:30px 0;\">" +
                    codeBoxes +
                "</div>" +

                "<!-- Divider -->" +
                "<div style=\"text-align:center;margin:25px 0;color:rgba(233,69,96,0.4);font-size:14px;letter-spacing:8px;\">" +
                    "✦ ✦ ✦" +
                "</div>" +

                "<!-- Timer info -->" +
                "<div style=\"background:rgba(233,69,96,0.08);border:1px solid rgba(233,69,96,0.2);" +
                    "border-radius:12px;padding:16px 20px;margin:0 0 20px;\">" +
                    "<p style=\"margin:0;font-size:14px;color:#b0b0b0;text-align:center;\">" +
                        "⏳ Код действителен <span style=\"color:#e94560;font-weight:600;\">5 минут</span>" +
                    "</p>" +
                "</div>" +

                "<!-- Warning -->" +
                "<p style=\"margin:0;font-size:13px;color:#666;text-align:center;line-height:1.6;\">" +
                    "Если вы не запрашивали этот код, просто проигнорируйте это письмо." +
                "</p>" +

            "</td></tr>" +

            "<!-- Footer -->" +
            "<tr><td style=\"padding:20px 40px 30px;\">" +
                "<div style=\"border-top:1px solid rgba(233,69,96,0.15);padding-top:20px;text-align:center;\">" +

                    "<div style=\"font-size:18px;letter-spacing:6px;margin-bottom:10px;\">🌸🏯🌸</div>" +

                    "<p style=\"margin:0 0 4px;font-size:12px;color:#555;\">" +
                        "© Tech Forge 2026" +
                    "</p>" +
                    "<p style=\"margin:0;font-size:11px;color:#444;letter-spacing:2px;\">" +
                        "日本のテクノロジー ・ 品質と信頼" +
                    "</p>" +

                "</div>" +
            "</td></tr>" +

            "</table>" +

            "</td></tr>" +
            "</table>" +
            "</body></html>";
    }
}
