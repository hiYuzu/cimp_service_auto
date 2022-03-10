package com.sinosoft.cimp_service.task;

import cn.hutool.http.HttpUtil;
import com.sinosoft.cimp_service.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author hiYuzu
 * @version V1.0
 * @date 2022/1/24 16:28
 */
@Component
public class CimpTask2 {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private String contentPortal;
    private String contentCenter;
    private String contentCert;
    private String contentHall;
    private String contentFile;
    Map<String, Object> params;
    private boolean first;
    private boolean active;

    CimpTask2() {
        params = new HashMap<>(2);
        params.put("valiCode", "1234");
        first = true;
        active = true;
    }

    @PostConstruct
    @Scheduled(cron = "0 */30 * * * ?")
    public void getCimp() {
        if (!isActive()) {
            return;
        }
        String content1 = HttpUtil.get("https://credit.jdzx.net.cn/portal/");
        String content2 = HttpUtil.get("https://www.jdzx.net.cn/");
        String content3 = HttpUtil.get("https://slps.jdzx.net.cn/index_Cert.jsp");
        String content4 = HttpUtil.get("https://slps.jdzx.net.cn/");
        String content5 = HttpUtil.get("https://slps.jdzx.net.cn/xwfb/gzcx/PassFileQuery.jsp");
        if (first) {
            LOG.info("初始化...");
            contentPortal = content1;
            contentCenter = content2;
            contentCert = content3;
            contentHall = content4;
            contentFile = content5;
            first = false;
            try {
                EmailUtil.send("Info", "网站监控服务已启动，这是一封通知邮件。");
            } catch (MessagingException | GeneralSecurityException e) {
                e.printStackTrace();
                LOG.error("邮件发送抛出 " + e.getClass().getName() + " 异常：" + e.getMessage());
                this.setActive(false);
            }
            return;
        }
        if (!contentPortal.equals(content1)) {
            try {
                EmailUtil.send("Warning", "请检查链接<a>https://credit.jdzx.net.cn/portal/</a>是否正常");
                LOG.warn("比对出现不同，请检查信用信息网是否正常。");
            } catch (MessagingException | GeneralSecurityException e) {
                e.printStackTrace();
                LOG.error("邮件发送抛出 " + e.getClass().getName() + " 异常：" + e.getMessage());
            } finally {
                this.setActive(false);
            }
        }
        if(!contentCenter.equals(content2)) {
            try {
                EmailUtil.send("Warning", "请检查链接<a>https://www.jdzx.net.cn/</a>是否正常");
                LOG.warn("比对出现不同，请检查监督中心网站是否正常。");
            } catch (MessagingException | GeneralSecurityException e) {
                e.printStackTrace();
                LOG.error("邮件发送抛出 " + e.getClass().getName() + " 异常：" + e.getMessage());
            } finally {
                this.setActive(false);
            }
        }
        if (!contentCert.equals(content3)) {
            try {
                EmailUtil.send("Warning", "请检查链接<a>https://slps.jdzx.net.cn/index_Cert.jsp</a>是否正常");
                LOG.warn("比对出现不同，请检查政务大厅是否正常。");
            } catch (MessagingException | GeneralSecurityException e) {
                e.printStackTrace();
                LOG.error("邮件发送抛出 " + e.getClass().getName() + " 异常：" + e.getMessage());
            } finally {
                this.setActive(false);
            }
        }
        if(!contentHall.equals(content4)) {
            try {
                EmailUtil.send("Warning", "请检查链接<a>https://slps.jdzx.net.cn/</a>是否正常");
                LOG.warn("比对出现不同，请检查政务大厅是否正常。");
            } catch (MessagingException | GeneralSecurityException e) {
                e.printStackTrace();
                LOG.error("邮件发送抛出 " + e.getClass().getName() + " 异常：" + e.getMessage());
            } finally {
                this.setActive(false);
            }
        }
        if(!contentFile.equals(content5)) {
            try {
                EmailUtil.send("Warning", "请检查链接<a>https://slps.jdzx.net.cn/xwfb/gzcx/PassFileQuery.jsp</a>是否正常");
                LOG.warn("比对出现不同，请检查政务大厅是否正常。");
            } catch (MessagingException | GeneralSecurityException e) {
                e.printStackTrace();
                LOG.error("邮件发送抛出 " + e.getClass().getName() + " 异常：" + e.getMessage());
            } finally {
                this.setActive(false);
            }
        }
        if (isActive()) {
            LOG.info("访问正常，本次测试结束。");
        } else {
            LOG.warn("检查功能已关闭，请手动重启。");
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
