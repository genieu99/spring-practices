package com.poscodx.guestbook.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.poscodx.guestbook.repository.template.JdbcContext;
import com.poscodx.guestbook.repository.template.StatementStrategy;
import com.poscodx.guestbook.vo.GuestbookVo;

@Repository
public class GuestbookRepositoryWithJdbcContext {
	
	private JdbcContext jdbcContext;
	
	public GuestbookRepositoryWithJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public int insert(GuestbookVo vo) {
		return jdbcContext.executeUpdate("insert into guestbook(name, password, contents, reg_date) values(?, ?, ?, now())",
				new Object[] {vo.getName(), vo.getPassword(), vo.getContents()}
		);
	}
	
	public int deleteByNoAndPassword(Long no, String password) {
		return jdbcContext.executeUpdate("delete from guestbook where no = ? and password = ?", new Object[] {no, password});
	}
}
