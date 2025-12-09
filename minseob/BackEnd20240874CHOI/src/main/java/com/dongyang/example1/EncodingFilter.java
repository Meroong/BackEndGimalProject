package com.dongyang.example1;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter extends HttpFilter implements Filter {
       
	
	public void destroy() {
		System.out.println("---------------encoding filter 소멸---------------");
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//전 
		request.setCharacterEncoding("utf-8");
		
		long begin = System.currentTimeMillis();

		chain.doFilter(request, response);
		
		long end = System.currentTimeMillis();
		System.out.println("작업시간 :"+ (end - begin) + "ms");
		//후
	
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("---------------encoding filter 생성---------------");
	}

}
