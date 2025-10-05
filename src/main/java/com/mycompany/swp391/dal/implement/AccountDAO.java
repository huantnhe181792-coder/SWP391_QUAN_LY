/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.swp391.dal.implement;


import com.mycompany.swp391.controller.authen.LoginServlet;
import com.mycompany.swp391.dal.DBContext;
import com.mycompany.swp391.dal.I_DAO;
import com.mycompany.swp391.entity.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dell
 */
public class AccountDAO extends DBContext implements I_DAO<Account> {

  @Override
  public List<Account> findAll() {
    List<Account> account = new ArrayList<>();
    try {
      connection = getConnection();
      String sql = "SELECT * FROM account";
      statement = connection.prepareStatement(sql);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        account.add(getFromResultSet(resultSet));
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return account;
  }

  @Override
  public Map<Integer, Account> findAllMap() {
    Map<Integer, Account> accountMap = new HashMap<>();
    try {
      connection = getConnection();
      String sql = "SELECT * FROM account";
      statement = connection.prepareStatement(sql);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        Account account = getFromResultSet(resultSet);
        accountMap.put(account.getId(), account);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return accountMap;
  }

  @Override
  public boolean update(Account t) {
    boolean result = false;
    try {
      connection = getConnection();
      String sql = "UPDATE account SET email=?, password=?, fullname=?, phone=?, gender=?, bod=?, updated_at=?, status=?, address=?, student_id=? WHERE id=?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, t.getEmail());
      statement.setString(2, t.getPassword());
      statement.setString(3, t.getFullname());
      statement.setString(4, t.getPhone());
      statement.setString(5, t.getGender());
      statement.setDate(6, new java.sql.Date(t.getBod().getTime()));
      statement.setDate(7, new java.sql.Date(new java.util.Date().getTime())); // current date for updated_at
      statement.setString(8, t.getStatus());
      statement.setString(9, t.getAddress());
      statement.setString(10, t.getStudent_id());
      statement.setInt(11, t.getId());
      result = statement.executeUpdate() > 0;
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return result;
  }

  public boolean updatePassword(int id, String password) {
    boolean result = false;
    try {
      connection = getConnection();
      String sql = "UPDATE account SET password=? WHERE id=?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, password);
      statement.setInt(2, id);
      result = statement.executeUpdate() > 0;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      closeResources();
    }
    return result;
  }

  @Override
  public boolean delete(Account t) {
    boolean result = false;
    try {
      connection = getConnection();
      String sql = "DELETE FROM account WHERE id=?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, t.getId());
      result = statement.executeUpdate() > 0;
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return result;
  }

  @Override
  public int insert(Account t) {
    int generatedId = -1;
    try {
      connection = getConnection();
      String sql = "INSERT INTO account (email, password, fullname, phone, gender, bod, created_at, updated_at, status, address, student_id, confirmation_token, is_confirm) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      statement = connection.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);
      statement.setString(1, t.getEmail());
      statement.setString(2, t.getPassword());
      statement.setString(3, t.getFullname());
      statement.setString(4, t.getPhone());
      statement.setString(5, t.getGender());
      statement.setDate(6, t.getBod() != null ? new java.sql.Date(t.getBod().getTime()) : null);
      java.util.Date currentDate = new java.util.Date();
      java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
      statement.setDate(7, sqlDate); // created_at
      statement.setDate(8, sqlDate); // updated_at
      statement.setString(9, t.getStatus());
      statement.setString(10, t.getAddress());
      statement.setString(11, t.getStudent_id());
      statement.setString(12, t.getConfirmationToken());
      statement.setBoolean(13, t.isConfirm());

      int affectedRows = statement.executeUpdate();
      if (affectedRows > 0) {
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
          generatedId = rs.getInt(1);
        }
      }
      if (generatedId > 0) {
//        AccountClubDAO accountClubDAO = new AccountClubDAO();
//        AccountClub accountClub = new AccountClub();
//        accountClub.setAccount_id(generatedId);
//        accountClub.setRole("user");
//        accountClubDAO.insert(accountClub);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return generatedId;
  }

  @Override
  public Account findById(Integer id) {
    Account account = null;
    try {
      connection = getConnection();
      String sql = "SELECT * FROM account WHERE id=?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      resultSet = statement.executeQuery();
      if (resultSet.next()) {
        account = getFromResultSet(resultSet);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return account;
  }


  public Account findByEmail(String email) {
    Account account = null;
    try {
      connection = getConnection();
      String sql = "SELECT * FROM account WHERE email=?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, email);
      resultSet = statement.executeQuery();
      if (resultSet.next()) {
        account = getFromResultSet(resultSet);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return account;
  }

  @Override
  public Account getFromResultSet(ResultSet rs) throws SQLException {
    Account account = new Account();
    account.setId(rs.getInt("id"));
    account.setEmail(rs.getString("email"));
    account.setPassword(rs.getString("password"));
    account.setFullname(rs.getString("fullname"));
    account.setPhone(rs.getString("phone"));
    account.setGender(rs.getString("gender"));
    account.setBod(rs.getDate("bod"));
    account.setCreated_at(rs.getDate("created_at"));
    account.setUpdated_at(rs.getDate("updated_at"));
    account.setStatus(rs.getString("status"));
    account.setAddress(rs.getString("address"));
    account.setStudent_id(rs.getString("student_id"));
    try { account.setConfirmationToken(rs.getString("confirmation_token")); } catch (SQLException ignore) {}
    try { account.setConfirm(rs.getBoolean("is_confirm")); } catch (SQLException ignore) {}
    return account;
  }

  public Account findByConfirmationToken(String token) {
    Account account = null;
    try {
      connection = getConnection();
      String sql = "SELECT * FROM account WHERE confirmation_token = ?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, token);
      resultSet = statement.executeQuery();
      if (resultSet.next()) {
        account = getFromResultSet(resultSet);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return account;
  }

  public boolean confirmByToken(String token) {
    boolean result = false;
    try {
      connection = getConnection();
      String sql = "UPDATE account SET is_confirm = 1, confirmation_token = NULL, updated_at = ? WHERE confirmation_token = ?";
      statement = connection.prepareStatement(sql);
      statement.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
      statement.setString(2, token);
      result = statement.executeUpdate() > 0;
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return result;
  }

  public static void main(String[] args) {
    Account ac = new Account();
    AccountDAO acdao = new AccountDAO();
    ac.setEmail("anhpld@gmail.com");
    ac.setFullname("Duc Anh");
    ac.setAddress("HANOI");
    ac.setGender("male");
    ac.setPassword(LoginServlet.hashPassword("12345"));
    ac.setStudent_id("SE05018");
    ac.setStatus("active");
    System.out.println(acdao.insert(ac));
  }

  public Account findLastId() {
    Account account = null;
    try {
      connection = getConnection();
      String sql = "SELECT * FROM account ORDER BY id DESC LIMIT 1";
      statement = connection.prepareStatement(sql);
      resultSet = statement.executeQuery();
      if (resultSet.next()) {
        account = getFromResultSet(resultSet);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return account;
  }
}
