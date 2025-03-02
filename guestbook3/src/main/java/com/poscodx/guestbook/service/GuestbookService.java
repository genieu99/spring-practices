package com.poscodx.guestbook.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.poscodx.guestbook.repository.GuestbookLogRepository;
import com.poscodx.guestbook.repository.GuestbookRepository;
import com.poscodx.guestbook.vo.GuestbookVo;

@Service
public class GuestbookService {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private GuestbookRepository guestbookRepository;
	
	@Autowired
	private GuestbookLogRepository guestbookLogRepository;
	
	public List<GuestbookVo> getContentsList() {
		return guestbookRepository.findAll();
	}
	
	public void deleteContents(Long no, String password) {
		// Transaction: BEGIN //////////
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {	
			guestbookLogRepository.update(no);
			guestbookRepository.deleteByNoAndPassword(no, password);
			
			// Transaction: END(SUCCESS) ////////
			transactionManager.commit(status);
		} catch(Throwable e) {
			// Transaction: END(FAIL) ////////
			transactionManager.rollback(status);
		}
	}
	
	public void addContents(GuestbookVo vo) {
		// 트랜잭션 동기(Connection) 초기
		TransactionSynchronizationManager.initSynchronization();
		
		Connection conn = DataSourceUtils.getConnection(dataSource);
		
		try {
			// Transaction: BEGIN //////////
			conn.setAutoCommit(false);
			
			int count = guestbookLogRepository.update();
			
			if (count == 0) {
				guestbookLogRepository.insert();
			}
			
			guestbookRepository.insert(vo);
			
			// Transaction: END(SUCCESS) ////////
			conn.commit();
			
		} catch(Throwable e) {
			// Transaction: END(FAIL) ////////
			try {
				conn.rollback();
			} catch (SQLException ignored) {
			} finally {
				DataSourceUtils.releaseConnection(conn, dataSource);
			}
		}
	}
}
