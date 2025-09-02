package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.Book;
import com.example.demo.dto.Pager;

@Mapper
public interface BookDao {
    public int insert(Book book);
    public Book selectByBid(int bid);
    public List<Book> selectByPage(Pager pager);
    public int update(Book book);
    public int delete(int bid);
    public int countAll();
}
