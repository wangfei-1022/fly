package com.wf.system.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class CaptchaUtil {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 75;
    private static final int FONT_SIZE = 36;
    private static final String DEFAULT_FONT = "Arial";

    /**
     * 生成验证码图像.
     *
     * @param captchaText 验证码原始文本
     * @return Base64编码的图像字符串
     */
    public static String generateCaptchaImage(String captchaText) {
        if (captchaText == null || captchaText.isEmpty()) {
            throw new IllegalArgumentException("Captcha text cannot be null or empty.");
        }

        // 创建图像和图形上下文
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        // 设置背景颜色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 绘制验证码文本
        g.setFont(new Font(DEFAULT_FONT, Font.BOLD, FONT_SIZE));
        g.setColor(getRandomColor());
        g.drawString(captchaText, 45, 50);

        // 添加随机线条作为干扰
        addNoiseLines(g);

        // 关闭图形上下文
        g.dispose();

        // 将图像转换为Base64编码的字符串
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generating captcha image", e);
        }
    }

    private static void addNoiseLines(Graphics2D g) {
        for (int i = 0; i < 5; i++) {
            g.setColor(getRandomColor());
            g.drawLine(
                    getRandomNumber(WIDTH),
                    getRandomNumber(HEIGHT),
                    getRandomNumber(WIDTH),
                    getRandomNumber(HEIGHT)
            );
        }
    }

    private static Color getRandomColor() {
        return new Color((int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255));
    }

    private static int getRandomNumber(int bound) {
        return (int) (Math.random() * bound);
    }
}