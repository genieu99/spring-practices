package com.poscodx.guestbook.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.poscodx.guestbook.vo.GuestbookVo;

@Repository
public class GuestbookRepositoryWithRawJdbc {
	private DataSource dataSource;
	
	public GuestbookRepositoryWithRawJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void insert(GuestbookVo mockGuestbookVo) {
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into guestbook(name, password, contents, reg_date) values(?, ?, ?, now())");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			pstmt1.setString(1, mockGuestbookVo.getName());
			pstmt1.setString(2, mockGuestbookVo.getPassword());
			pstmt1.setString(3, mockGuestbookVo.getContents());
			pstmt1.executeUpdate();
		
			ResultSet rs = pstmt2.executeQuery();
			mockGuestbookVo.setNo(rs.next() ? rs.getLong(1) : null);
			rs.close();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}
	
	public List<GuestbookVo> findAll() {
		List<GuestbookVo> result = new ArrayList<>();
		
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select no, name, date_format(reg_date, '%Y-%m-%d'), contents from guestbook order by reg_date desc");
				ResultSet rs = pstmt.executeQuery();
		) {
			while (rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String reg_date = rs.getString(3);
				String contents = rs.getString(4);
				
				GuestbookVo guestbookVo = new GuestbookVo();
				guestbookVo.setNo(no);
				guestbookVo.setName(name);
				guestbookVo.setContents(contents);
				guestbookVo.setRegDate(reg_date);
				
				result.add(guestbookVo);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		
		return result;
	}

	public void deleteByNoAndPassword(Long no, String password) {
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from guestbook where no = ? and password = ?");
		) {
			pstmt.setLong(1, no);
			pstmt.setString(2, password);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}
}
