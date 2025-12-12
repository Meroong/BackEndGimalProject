package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.FileResourceDTO;
import util.JDBCUtil;

public class FileResourceDAO {
	//단일 파일 조회용 프로필용
	public String getFileUrl(long autoId, String usedType) {
		System.out.println("work DBquery: getFileUrl");
		String sql = "select file_url from file_resource where used_id = ? and used_type = ?;";
		String oldFileName = "";
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
		
        	pstmt.setLong(1, autoId);
        	pstmt.setString(2, usedType);
        	
        	ResultSet rs = pstmt.executeQuery();
        	
        	if(rs.next()) {
        		oldFileName = rs.getString("file_url");
        	}
        	return oldFileName;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
            return oldFileName;
       }
	} 
	
	//다중 파일 조회용 모임용
	public List<FileResourceDTO> getFileUrls(long autoId, String usedType) {
		System.out.println("work DBquery: getFileUrl");
		List<FileResourceDTO> alist = new ArrayList<>();
		String sql = "select id, file_url from file_resource where used_id = ? and used_type = ?;";
		
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
		
        	pstmt.setLong(1, autoId);
        	pstmt.setString(2, usedType);
        	
        	ResultSet rs = pstmt.executeQuery();
        	
        	while(rs.next()) {
        		FileResourceDTO dto = new FileResourceDTO();
        		
        		dto.setId(rs.getLong("id"));
        		dto.setFileUrl(rs.getString("file_url"));
        		alist.add(dto);
        	}
        	return alist;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
            return alist;
       }
	}
	
	public List<String> getPostUrls(long autoId, long dreamId, String usedType) {
		System.out.println("work DBquery: getFileUrl");
		List<String> alist = new ArrayList<>();
		String sql = "select id, file_url from file_resource where used_id = ? and used_type = ? and file_url like '/upload/POST/"+ dreamId+"%';";
		
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
		
        	pstmt.setLong(1, autoId);
        	pstmt.setString(2, usedType);
        	
        	ResultSet rs = pstmt.executeQuery();
        	
        	while(rs.next()) {
        		alist.add(rs.getString("file_url"));
        	}
        	return alist;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
            return alist;
       }
	}
	
	
	public boolean insertFile(FileResourceDTO dto) {
		System.out.println("work DBquery: insertFile");
		
		System.out.println("INSERT PARAMS:");
	    System.out.println("fileUrl = " + dto.getFileUrl());
	    System.out.println("fileName = " + dto.getFileName());
	    System.out.println("originalName = " + dto.getOriginalName());
	    System.out.println("fileType = " + dto.getFileType());
	    System.out.println("size = " + dto.getSize());
	    System.out.println("usedType = " + dto.getUsedType());
	    System.out.println("usedId = " + dto.getUsedId());
		String sql = "Insert into file_resource(file_url, file_name, original_name,"
				+ " file_type, size, used_type, used_id) values(?, ?, ?, ?, ?, ?, ?);";
		
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
           
            pstmt.setString(1, dto.getFileUrl());
            pstmt.setString(2, dto.getFileName());
            pstmt.setString(3, dto.getOriginalName());
   			pstmt.setString(4, dto.getFileType()); 
   			pstmt.setLong(5, dto.getSize());
   			pstmt.setString(6, dto.getUsedType());
   			pstmt.setLong(7, dto.getUsedId());

   			int rs = pstmt.executeUpdate();
   			if(rs>0) {
   				System.out.println("업데이트 성공");
   				return true;
   			}
   			System.out.println("업데이트 실패");
   			return false;
           } catch (SQLException e) {
               e.printStackTrace();
               System.out.println("DB 연결 또는 쿼리 오류");
               return false;
          }
      }	
	// 아이디와 사용타입으로 사진을 전부 지울 때 사용 / 프로필 사진 삭제 혹은 모임 게시글 삭제 시   
	public boolean deleteFileByUsed(String usedType, long usedId) {
		System.out.println("work DBquery: deleteFile");
		String sql = "delete from file_resource where used_type = ? and used_id = ?";
		
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
        	
        	pstmt.setString(1, usedType);
            pstmt.setLong(2, usedId);

   			int rs = pstmt.executeUpdate();
   			if(rs>0) {
   				System.out.println("삭제 성공");
   				return true;
   			}
   			System.out.println("삭제 실패");
   			return false;
           } catch (SQLException e) {
               e.printStackTrace();
               System.out.println("DB 연결 또는 쿼리 오류");
               return false;
          }
	}
	//id컬럼으로 삭제 	|	타입과 사용자 확인으로 권한확인 
	public boolean deleteFileById(long fileId, long ownerId, String usedType) {
	    String sql =
	        "DELETE fr FROM file_resource fr " +
	        "JOIN meeting m ON fr.used_id = m.id AND fr.used_type = ? " +
	        "WHERE fr.id = ? AND m.creator_id = ?";

	    try (Connection con = JDBCUtil.jdbcCon();
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        pstmt.setString(1, usedType);
	        pstmt.setLong(2, fileId);
	        pstmt.setLong(3, ownerId);

	        if(pstmt.executeUpdate() > 0) {
	        	System.out.println("파일 삭제 성공");
	        	return true;
	        }
	        else {
	        	System.out.println("파일 삭제 실패");
	        	return false;
	        }

	    } catch (Exception e) {
	    	System.out.println("DB 에러");
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public boolean deletePostFileByUrl(long usedId, String usedType, String fileUrl) {
	    System.out.println("work DBquery: deletePostFileByUrl");
	    String sql = "DELETE FROM file_resource WHERE used_type = ? AND used_id = ? AND file_url = ?";

	    try (Connection con = JDBCUtil.jdbcCon();
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        pstmt.setString(1, usedType);
	        pstmt.setLong(2, usedId);
	        pstmt.setString(3, fileUrl);

	        int rs = pstmt.executeUpdate();
	        if (rs > 0) {
	            System.out.println("삭제 성공");
	            return true;
	        }
	        System.out.println("삭제 실패");
	        return false;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("DB 연결 또는 쿼리 오류");
	        return false;
	    }
	}
	
	public boolean isExist(long autoId, String usedType) {
		System.out.println("work DBquery: isExist");
		String sql = "select * from file_resource where used_type = ? and used_id = ?;";
		
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
		
        	pstmt.setString(1, usedType);
        	pstmt.setLong(2, autoId);
        	
        	ResultSet rs = pstmt.executeQuery();
        	
        	if(rs.next()) {
        		return true;
        	}
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
            return false;
       }
		
		return false;
	}
}

