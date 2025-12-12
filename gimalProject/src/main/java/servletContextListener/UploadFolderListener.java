package servletContextListener;

import java.io.File;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

//이미지 저장 폴더를 외부로 빼서 다른 환경에서는 매번 폴더를 생성해야하는 번거로움 
//-> 3.0이 지원하는 리스너 사용 서버 생성, 종료 시 실행되는 리스너 활용해서 폴더 만들기
@WebListener
public class UploadFolderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String baseUploadPath = "C:/upload";
        File uploadDir = new File(baseUploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            System.out.println("업로드 기본 폴더 생성: " + created);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 서버 종료 시 처리 필요 시 작성
    }
}