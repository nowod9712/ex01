package com.study.erum.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.erum.dto.MemberDTO;
import com.study.erum.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member") // 작업하는 모든 파일은 member로 시작하라.
@RequiredArgsConstructor
public class MemberController {

	
	private final MemberService memberService;//*생성자 주입 MemberService로 연결되어 있음.
	
	@GetMapping("/save")//보여주는 부분
	public String saveForm() {
		return "save";
	}
	
	@PostMapping("/save")//저장하는 부분
	public String save(@ModelAttribute MemberDTO memberDTO) {
		int saveResult = memberService.save(memberDTO);
			if(saveResult > 0) {
				return "login";
			}else {
				return "save";
			}
	}
	
	@GetMapping("/login")//보여주는 부분
	public String loginForm() {
		return "login";
	}
		 
	@PostMapping("/login")//저장하는 부분
	public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
		boolean loginResult = memberService.login(memberDTO);
			if(loginResult) {
				session.setAttribute("loginEmail", memberDTO.getMemberEmail());
				return "main";
			}else {
				return "login";
			}
	}
	
	@GetMapping("/")//보여주는 부분
	public String findAll(Model model) {
		List<MemberDTO> memberDTOList = memberService.findAll();// 실제로는 limit 이용해서 필요한 페이지 불러오기.
		model.addAttribute("memberList", memberDTOList);
		return "list";
	}
	
	// /member?id=1
	  @GetMapping
	  public String findById(@RequestParam("id") Long id, Model model){
	    MemberDTO memberDTO = memberService.findById(id); // 한 개의 데이터 가져오기
	    model.addAttribute("member", memberDTO);
	    return "detail";// 데이터를 가져와서 member한테 줘서 detail에 뿌린다.
	  }
	  
	  @GetMapping("/delete")
	  public String delete(@RequestParam("id") Long id){
		  memberService.delete(id);
		  return "redirect:/member/";
	  }
	  
	  //수정화면 요청
	  @GetMapping("/update")
	  public String updateForm(HttpSession session, Model model) {
		  //세션에 저장된 나의 이메일 가져오기
		  String loginEmail =(String)session.getAttribute("loginEmail");//강제 형변환, 캐스캐이드 연산자
		  MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
		  model.addAttribute("member", memberDTO);
		  
		  return "update";
		  
	  }
	  
	 @PostMapping("/update")//PRG(Post-Redirect-Get) 패턴
	 public String update(@ModelAttribute MemberDTO memberDTO){
		 boolean result = memberService.update(memberDTO);
		 if(result) {
			 return "redirect:/member?id=" + memberDTO.getId();
		 }else {
			 return "index";
		 }
	 }
	 
	 @PostMapping("/email-check")
	 public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
		 System.out.println("memberEmail =" + memberEmail);
		 String checkResult = memberService.emailCheck(memberEmail);
		 return checkResult;
	 }
	 
	 @GetMapping("/logout")//보여주는 부분
		public String logout() {
			return "logout";
		}
	 
}
