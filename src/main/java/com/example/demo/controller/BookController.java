package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.Book;
import com.example.demo.dto.Pager;
import com.example.demo.service.BookService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/db")
@Slf4j
public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping("/book-create")
    public Book bookCreate(Book book) throws Exception {
        log.info(book.toString());
        MultipartFile mf = book.getBattach();
        if (mf != null && !mf.isEmpty()) {
            book.setBattachoname(mf.getOriginalFilename());
            book.setBattachtype(mf.getContentType());
            book.setBattachdata(mf.getBytes());
        }

        bookService.create(book);

        Book dbBook = bookService.getBook(book.getBid());

        return dbBook;
    }

    @GetMapping("/book-detail")
    public Book bookDetail(@RequestParam("bid") int bid) {
        Book book = bookService.getBook(bid);

        return book;
    }

    @GetMapping("/book-list")
    public Map<String, Object> bookList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        log.info("pageNo" + pageNo);
        int totalRows = bookService.getTotalRows();
        Pager pager = new Pager(10, 10, totalRows, pageNo);

        List<Book> list = bookService.getListByPage(pager);

        Map<String, Object> map = new HashMap<>();
        map.put("pager", pager);
        map.put("book", list);
        return map;
    }

    @PutMapping("/modify")
    public Map<String, Object> modify(Book book) throws IOException {
        log.info(book.toString());
        Book dbBook = bookService.modify(book);
        Map<String, Object> map = new HashMap<>();
        if (dbBook == null) {
            map.put("result", "fail");
        } else {
            map.put("result", "success");
            map.put("member", dbBook);
        }
        return map;
    }

    @GetMapping("/attach-download")
    public void attachDownload(@RequestParam("bid") int bid, HttpServletResponse response) throws Exception {
        Book book = bookService.getBook(bid);

        String fileName = book.getBattachoname();

        // 응답 헤더에 Content-Type 추가
        response.setContentType(book.getBattachtype());
        // response.setHeader("Content-Type", book.getBattachtype());

        // 본문 내용을 파일로 저장할 수 있도록 헤더 추가
        String encodedFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment; file=\"" + encodedFileName + "\"");

        // 응답 본문으로 데이터를 출력하는 스트림
        OutputStream os = response.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);

        // 응답 본문에 파일 데이터 출력
        bos.write(book.getBattachdata());
        bos.flush();
        bos.close();
    }

    @DeleteMapping("/book-delete")
    public String bookDelete(@RequestParam("bid") int bid) {
        bookService.remove(bid);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "삭제 완료");

        return jsonObject.toString();
    }
}
