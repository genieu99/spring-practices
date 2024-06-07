package com.poscodx.guestbook.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.poscodx.guestbook.repository.template.JdbcContext;
import com.poscodx.guestbook.vo.GuestbookVo;

@Repository
public class GuestbookRepository {
	
	private JdbcContext jdbcContext;
	
	public GuestbookRepository(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public List<GuestbookVo> findAll() {
		return jdbcContext.query(
			"select no, name, date_format(reg_date, '%Y-%m-%d %H:%i:%s'), contents from guestbook order by reg_date desc",
			new RowMapper<GuestbookVo>() {
				@Override
				public GuestbookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					GuestbookVo vo = new GuestbookVo();
					vo.setNo(rs.getLong(1));
					vo.setName(rs.getString(2));
					vo.setRegDate(rs.getString(3));
					vo.setContents(rs.getString(4));
					return vo;
				}
			});
	}
	
	public int insert(GuestbookVo vo) {
		return jdbcContext.update("insert into guestbook(name, password, contents, reg_date) values(?, ?, ?, now())",
				new Object[] {vo.getName(), vo.getPassword(), vo.getContents()}
		);
	}
	
	public int deleteByNoAndPassword(Long no, String password) {
		return jdbcContext.update("delete from guestbook where no = ? and password = ?", new Object[] {no, password});
	}
}
