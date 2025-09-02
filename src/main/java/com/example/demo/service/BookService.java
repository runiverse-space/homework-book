package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.BookDao;
import com.example.demo.dto.Book;
import com.example.demo.dto.Pager;

@Service
public class BookService {
    @Autowired
    BookDao bookDao;

    public void create(Book book) {
        bookDao.insert(book);
    }

    public Book getBook(int bid) {
        Book book = bookDao.selectByBid(bid);
        return book;
    }

    public List<Book> getListByPage(Pager pager) {
        List<Book> list = bookDao.selectByPage(pager);

        return list;
    }

    public int getTotalRows() {
        int totalRows = bookDao.countAll();
        return totalRows;
    }

    public Book modify(Book book) throws IOException {
        Book dbBook = bookDao.selectByBid(book.getBid());
        if (dbBook == null) {
            return null;
        } else {
            if (StringUtils.hasText(book.getBtitle())) {
                dbBook.setBtitle(book.getBtitle());
            }
            if (StringUtils.hasText(book.getBpublisher())) {
                dbBook.setBpublisher(book.getBpublisher());
            }
            if (StringUtils.hasText(book.getBprice() + "")) {
                dbBook.setBprice(book.getBprice());
            }
            if (StringUtils.hasText(book.getBauthor())) {
                dbBook.setBauthor(book.getBauthor());
            }

            MultipartFile mf = book.getBattach();

            if (mf != null && !mf.isEmpty()) {
                dbBook.setBattachoname(mf.getOriginalFilename());
                dbBook.setBattachtype(mf.getContentType());
                dbBook.setBattachdata(mf.getBytes());
            }
        }
        bookDao.update(dbBook);
        dbBook = bookDao.selectByBid(book.getBid());
        return dbBook;
    }

    public int remove(int bid) {
        int rows = bookDao.delete(bid);
        return rows;
    }

}
