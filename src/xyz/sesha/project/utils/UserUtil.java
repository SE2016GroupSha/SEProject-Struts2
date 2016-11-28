package xyz.sesha.project.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

/**
 * User������
 * <p>������Ҫ�ṩ���û���½���ֵ�֧�֣���װ��cookie��session<br>
 * @author Administrator
 */
public class UserUtil {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserUtil.class);
  
  public static String SHA1(String decript) {
    try {
        MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
        digest.update(decript.getBytes());
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        // �ֽ�����ת��Ϊ ʮ������ ��
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return "";
  }

  
  public static String getUserId() {
    String ret = null;
    
    boolean isExist = false;
    Cookie[] cookies = ServletActionContext.getRequest().getCookies();
    Map<String, Object> session = ActionContext.getContext().getSession();
    
    if (cookies!=null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("userinfo")) {
          if (session.containsKey(cookie.getValue())) {
            logger.info("[API][UserUtil/getUserId][��֤�ɹ�]" + cookie.getValue());
            isExist = true;
            ret = (String) session.get(cookie.getValue());
            break;
          }
        }
      }
    }
    
    if (!isExist) {
      logger.info("[API][UserUtil/getUserId][��֤ʧ��]");
    }

    return ret;
  }
  
  public static void addUserId(String id) {
    Cookie cookie = new Cookie("userinfo", SHA1(id));
    cookie.setMaxAge(60 * 60 * 24 * 14); // cookie�������� 
    cookie.setPath("/");
    ServletActionContext.getResponse().addCookie(cookie);
    ActionContext.getContext().getSession().put(SHA1(id), id);
    logger.info("[API][UserUtil/addUserId][��ӳɹ�]");
  }
  
  public static boolean delUserId() {
    Cookie[] cookies = ServletActionContext.getRequest().getCookies();
    Map<String, Object> session = ActionContext.getContext().getSession();
    
    if (cookies!=null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("userinfo")) {
          if (session.containsKey(cookie.getValue())) {

            session.remove(cookie.getValue());
            Cookie newCookie = new Cookie("userinfo", null);
            newCookie.setMaxAge(0);
            newCookie.setPath("/");
            ServletActionContext.getResponse().addCookie(newCookie);
            
            logger.info("[API][UserUtil/delUserId][ɾ���ɹ�]");
            return true;
          }
        }
      }
    }
    
    logger.info("[API][UserUtil/delUserId][ɾ��ʧ��]");
    return false;
  }
}
