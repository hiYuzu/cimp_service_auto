package com.sinosoft.cimp_service.task;

import cn.hutool.http.HttpUtil;
import com.sinosoft.cimp_service.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author hiYuzu
 * @version V1.0
 * @date 2021/9/10 15:47
 */
@Component
public class CimpTask {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private String contentCimp;
    Map<String, Object> params;
    private boolean first;
    private boolean active;

    CimpTask() {
        params = new HashMap<>(2);
        params.put("valiCode", "1234");
        first = true;
        active = true;
    }

    @Scheduled(cron = "0 */10 * * * ?")
    public void getCimp() {
        if (!isActive()) {
            return;
        }
        String content = HttpUtil.get("https://credit.jdzx.net.cn/cimp/login.do");
        if (first) {
            LOG.info("初始化...");
            contentCimp = content;
            first = false;
            return;
        }
        if (!contentCimp.equals(content)) {
            try {
                EmailUtil.send("Warning", "请检查链接<a>https://credit.jdzx.net.cn/cimp</a>是否正常");
                LOG.warn("比对出现不同，请检查链接是否正常。");
            } catch (MessagingException | GeneralSecurityException e) {
                e.printStackTrace();
                LOG.error("邮件发送抛出 " + e.getClass().getName() + " 异常：" + e.getMessage());
            } finally {
                this.setActive(false);
                LOG.warn("检查功能已关闭，请手动重启。");
            }
        } else {
            LOG.info("登录页访问正常，现在尝试请求服务端...");
            String result = HttpUtil.post("https://credit.jdzx.net.cn/cimp/encrypt/rsaKey.do", params);
            String resultStart = "{\"modulus\":";
            if (result.startsWith(resultStart)) {
                LOG.info("服务端正常响应，本次测试结束。");
            } else {
                try {
                    EmailUtil.send("Warning", "请检查信用信息管理平台服务端是否正常");
                    LOG.warn("服务端异常，请进一步检查。");
                } catch (MessagingException | GeneralSecurityException e) {
                    e.printStackTrace();
                    LOG.error("邮件发送抛出 " + e.getClass().getName() + " 异常：" + e.getMessage());
                } finally {
                    this.setActive(false);
                    LOG.warn("检查功能已关闭，请手动重启。");
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
