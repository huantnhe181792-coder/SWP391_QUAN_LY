/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.swp391.dal.implement;

import com.fall25.sp.swp.quanly.entity.Club;
import com.mycompany.swp391.dal.DBContext;
import com.mycompany.swp391.dal.I_DAO;
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
public class ClubDAO extends DBContext implements I_DAO<Club> {

  @Override
  public List<Club> findAll() {
    List<Club> listClub = new ArrayList<>();
    try {
      connection = getConnection();
      String sql = "SELECT * FROM club";
      statement = connection.prepareStatement(sql);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        listClub.add(getFromResultSet(resultSet));
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return listClub;
  }

  @Override
  public Map<Integer, Club> findAllMap() {
    Map<Integer, Club> clubMap = new HashMap<>();
    try {
      connection = getConnection();
      String sql = "SELECT * FROM club";
      statement = connection.prepareStatement(sql);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        Club club = getFromResultSet(resultSet);
        clubMap.put(club.getId(), club);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return clubMap;
  }

  @Override
  public boolean update(Club club) {
    boolean success = false;
    try {
      connection = getConnection();
      String sql = "UPDATE club SET name = ?, description = ?, updated_at = ?, "
          + "president_id = ?, category_id = ?, status = ?, logo_img = ? "
          + "WHERE id = ?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, club.getName());
      statement.setString(2, club.getDescription());
      statement.setDate(3, club.getUpdated_at());
      statement.setInt(4, club.getPresident_id());
      statement.setInt(5, club.getCategory_id());
      statement.setString(6, club.getStatus());
      statement.setString(7, club.getLogo_img());
      statement.setInt(8, club.getId());

      int rowsAffected = statement.executeUpdate();
      success = rowsAffected > 0;
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return success;
  }

  @Override
  public boolean delete(Club club) {
    boolean success = false;
    try {
      connection = getConnection();
      String sql = "DELETE FROM club WHERE id = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, club.getId());

      int rowsAffected = statement.executeUpdate();
      success = rowsAffected > 0;
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return success;
  }

  @Override
  public int insert(Club club) {
    int generatedId = -1;
    try {
      connection = getConnection();
      String sql = "INSERT INTO club (name, description, created_at, updated_at, "
          + "president_id, category_id, status, logo_img) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      statement = connection.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);
      statement.setString(1, club.getName());
      statement.setString(2, club.getDescription());
      statement.setDate(3, club.getCreated_at());
      statement.setDate(4, club.getUpdated_at());
      statement.setInt(5, club.getPresident_id());
      statement.setInt(6, club.getCategory_id());
      statement.setString(7, club.getStatus());
      statement.setString(8, club.getLogo_img());

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected > 0) {
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
          generatedId = generatedKeys.getInt(1);
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return generatedId;
  }

  @Override
  public Club getFromResultSet(ResultSet resultSet) throws SQLException {
    Club club = new Club();
    club.setId(resultSet.getInt("id"));
    club.setName(resultSet.getString("name"));
    club.setDescription(resultSet.getString("description"));
    club.setCreated_at(resultSet.getDate("created_at"));
    club.setUpdated_at(resultSet.getDate("updated_at"));
    club.setPresident_id(resultSet.getInt("president_id"));
    club.setCategory_id(resultSet.getInt("category_id"));
    club.setStatus(resultSet.getString("status"));
    club.setLogo_img(resultSet.getString("logo_img"));
    return club;
  }

  @Override
  public Club findById(Integer id) {
    Club club = null;
    try {
      connection = getConnection();
      String sql = "SELECT * FROM club WHERE id = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      resultSet = statement.executeQuery();
      if (resultSet.next()) {
        club = getFromResultSet(resultSet);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return club;
  }

  public Club findByPresidentId(Integer id) {
    Club club = null;
    try {
      connection = getConnection();
      String sql = "SELECT * FROM club WHERE president_id = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      resultSet = statement.executeQuery();
      if (resultSet.next()) {
        club = getFromResultSet(resultSet);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      closeResources();
    }
    return club;
  }
  
    public static void main(String[] args) {
        for(Club club : new ClubDAO().findAll()){
            System.out.println(club.toString());
        }
    }

}
