package com.example.messageboad.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.messageboad.beans.CreateThreadBean;
import com.example.messageboad.beans.LoginBean;
import com.example.messageboad.beans.PostBean;
import com.example.messageboad.dto.MessageBoardDetailDto;
import com.example.messageboad.dto.MessageBoardDto;
import com.example.messageboad.service.MessageBoardService;

@Controller
@RequestMapping("/messageBoard")
public class MessageBoardController {

	@Autowired
	MessageBoardService messageBoardService;

	// 掲示板top
	@GetMapping("/index")
	public ModelAndView index(ModelAndView mav) {

		// 新規スレッドを６件表示
		ArrayList<MessageBoardDto> newMbLists = messageBoardService.SelectNewThreads();
		// 書き込み順にスレッドを10件取得
		ArrayList<MessageBoardDto> postCountMbLists = messageBoardService.SelectThreadsOrderPostCount();
		// 人気順にスレッドを10件表示
		ArrayList<MessageBoardDto> likeCountMbLists = messageBoardService.SelectThreadsOrderLikeCount();
		// カテゴリID・nameを取得
		ArrayList<MessageBoardDto> categoryMbLists = messageBoardService.selectCategory();

		mav.addObject("newMbLists", newMbLists);
		mav.addObject("postCountMbLists", postCountMbLists);
		mav.addObject("likeCountMbLists", likeCountMbLists);
		mav.addObject("categoryMbLists", categoryMbLists);
		mav.setViewName("messageBoard");
		return mav;
	}

	// 掲示板一覧画面
	@GetMapping("/list")
	public ModelAndView list(ModelAndView mav) {

		showList(0, mav);
		return mav;
	}

	// 掲示板一覧画面
	@GetMapping("/list/{page}")
	public ModelAndView listPaging(@PathVariable int page, ModelAndView mav) {

		showList(page, mav);
		return mav;
	}

	// 掲示板一覧画面（検索）
	@GetMapping("/list/search")
	public ModelAndView Search(@RequestParam String searchText, ModelAndView mav) {

		showSearchList(searchText, 0, mav);
		return mav;
	}

	// 掲示板一覧画面ページング（検索）
	@GetMapping("/list/search/{searchText}/{page}")
	public ModelAndView SearchPaging(@PathVariable("searchText") String searchText, @PathVariable("page") int page,
			ModelAndView mav) {

		showSearchList(searchText, page, mav);
		return mav;
	}

	// 掲示板一覧画面（カテゴリ）
	@GetMapping("/list/category/{categoryId}")
	public ModelAndView Category(@PathVariable int categoryId, ModelAndView mav) {

		showCategoryList(categoryId, 0, mav);
		return mav;
	}

	// 掲示板一覧画面ページング（カテゴリ）
	@GetMapping("/list/category/{categoryId}/{page}")
	public ModelAndView categoryPaging(@PathVariable("categoryId") int categoryId, @PathVariable("page") int page,
			ModelAndView mav) {

		showCategoryList(categoryId, page, mav);
		return mav;
	}

	// 掲示板詳細
	@GetMapping("/detail/{threadId}")
	public ModelAndView Detail(@PathVariable("threadId") int threadId, ModelAndView mav, HttpSession session) {

		LoginBean lb = (LoginBean) session.getAttribute("lb");
		showDetail(threadId, lb.getId(), 0, mav);
		mav.setViewName("messageBoardDetail");
		return mav;
	}

	// ページング遷移
	@GetMapping("/detail/{threadId}/{page}")
	public ModelAndView DetailPage(@PathVariable("threadId") int threadId, @PathVariable("page") int page,
			ModelAndView mav, HttpSession session) {

		LoginBean lb = (LoginBean) session.getAttribute("lb");
		showDetail(threadId, lb.getId(), page, mav);
		mav.setViewName("messageBoardDetail");
		return mav;
	}

	// スレッド作成画面
	@GetMapping("/threads/create")
	public String newThread(Model model) {
	    if (!model.containsAttribute("createThreadBean")) {
	        CreateThreadBean createThreadBean = new CreateThreadBean();
	        createThreadBean.setCategoryId(1);
	        model.addAttribute("createThreadBean", createThreadBean);
	    }

	    ArrayList<MessageBoardDto> postCountMbLists = messageBoardService.SelectThreadsOrderPostCount();
	    ArrayList<MessageBoardDto> categoryMbLists = messageBoardService.selectCategory();

	    model.addAttribute("postCountMbLists", postCountMbLists);
	    model.addAttribute("categoryMbLists", categoryMbLists);

	    return "createThread";
	}

	@PostMapping("/threads/create")
	public String createThread(@Valid @ModelAttribute CreateThreadBean createThreadBean,
			BindingResult bindingResult, ModelAndView mav,
			HttpSession session, RedirectAttributes redirectAttributes) {
		
		// エラーチェック
		if (bindingResult.hasErrors()) {
	        redirectAttributes.addFlashAttribute("createThreadBean", createThreadBean);
	        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createThreadBean", bindingResult);
	        return "redirect:/messageBoard/threads/create";
	    }

		LoginBean lb = (LoginBean) session.getAttribute("lb");
		MultipartFile uploadedFile = createThreadBean.getFile();
		String originalFilename = uploadedFile.getOriginalFilename();
		String ext = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			ext = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		String safeFilename = UUID.randomUUID().toString() + ext;

		try {
			// ファイルをアップロード
			if (!uploadedFile.isEmpty()) {
				// 一時保存（target/uploads）
				String tempDirPath = System.getProperty("user.dir") + "/target/uploads/";
				File tempDir = new File(tempDirPath);
				if (!tempDir.exists()) {
					tempDir.mkdirs();
				}
				Path tempFilePath = Paths.get(tempDirPath + safeFilename);
				uploadedFile.transferTo(tempFilePath.toFile());

				// 本保存先（src/main/resources/static/images）
				String finalDirPath = System.getProperty("user.dir") + "/src/main/resources/static/images/";
				File finalDir = new File(finalDirPath);
				if (!finalDir.exists()) {
					finalDir.mkdirs();
				}
				Path finalFilePath = Paths.get(finalDirPath + safeFilename);
				Files.copy(tempFilePath, finalFilePath, StandardCopyOption.REPLACE_EXISTING);
				Files.delete(tempFilePath); // 一時ファイルを削除
			} else {
				// ファイルがない場合、ファイル名にデフォルトサムネイル画像名をセット。
				safeFilename = "default.jpeg";
			}

			// スレッドをデータベースに保存
			boolean updated = messageBoardService.addThread(createThreadBean.getTitle(),
					createThreadBean.getDescription(), createThreadBean.getCategoryId(), safeFilename, lb.getId());
			if (!updated) {
				throw new RuntimeException("スレッドの保存に失敗しました");
			}

		} catch (IOException e) {
			e.printStackTrace();
			ArrayList<String> errorList = new ArrayList<>();
			errorList.add("ファイルがアップロードできませんでした");
			createThreadBean.setErrorList(errorList);
			redirectAttributes.addFlashAttribute("cb", createThreadBean);
			return "redirect:/messageBoard/threads/create";
		}
		// 画像のアップロードが終わった後、遷移
		return "redirect:/messageBoard/index";
	}

	// 投稿
	@PostMapping("/detail/addPost")
	public String addPost(@ModelAttribute PostBean postBean, HttpSession session,
			RedirectAttributes redirectAttributes) {

		int threadId = postBean.getThreadId();
		LoginBean lb = (LoginBean) session.getAttribute("lb");
		String redirectUrl = "redirect:/messageBoard/detail/" + postBean.getThreadId() + "/" + postBean.getPage();

		// ポストを投稿
		if (!messageBoardService.addPost(threadId, postBean.getContent(), lb.getId())) {
			// 更新失敗した場合リダイレクト
			ArrayList<String> errorList = new ArrayList<String>();
			errorList.add("投稿できませんでした");
			postBean.setErrorList(errorList);
			redirectAttributes.addFlashAttribute("pb", postBean);
			return redirectUrl;
		}

		return redirectUrl;
	}

	// いいね
	@PostMapping("/detail/addLike/{postId}")
	public String addLike(@PathVariable("postId") int postId, PostBean postBean,
			HttpSession session, RedirectAttributes redirectAttributes) {

		LoginBean lb = (LoginBean) session.getAttribute("lb");
		String redirectUrl = "redirect:/messageBoard/detail/" + postBean.getThreadId() + "/" + postBean.getPage();

		// いいね済みかチェックしてから更新を行う
		if (messageBoardService.hasLike(postId, lb.getId())) {
			// すでにいいね済み
			ArrayList<String> errorList = new ArrayList<String>();
			errorList.add("すでにいいね済みです");
			postBean.setErrorList(errorList);
			redirectAttributes.addFlashAttribute("pb", postBean);
			return redirectUrl;
		} else if (!messageBoardService.addLike(postBean.getThreadId(), postId, lb.getId())) {
			// いいね失敗
			ArrayList<String> errorList = new ArrayList<String>();
			errorList.add("いいねできませんでした");
			postBean.setErrorList(errorList);
			redirectAttributes.addFlashAttribute("pb", postBean);
			return redirectUrl;
		}

		return redirectUrl;
	}

	// 共通処理
	public void showList(int page, ModelAndView mav) {
		// リクエストパラメータでスレッド名を検索
		Page<MessageBoardDto> mbLists = messageBoardService.selectThreadList(page, 20);
		// 書き込み順にスレッドを10件取得
		ArrayList<MessageBoardDto> postCountMbLists = messageBoardService.SelectThreadsOrderPostCount();
		// カテゴリID・nameを取得
		ArrayList<MessageBoardDto> categoryMbLists = messageBoardService.selectCategory();

		mav.addObject("mode", "list");
		mav.addObject("mbLists", mbLists);
		mav.addObject("postCountMbLists", postCountMbLists);
		mav.addObject("categoryMbLists", categoryMbLists);
		mav.setViewName("messageBoardList");
	}

	public void showSearchList(String searchText, int page, ModelAndView mav) {
		// リクエストパラメータでスレッド名を検索
		Page<MessageBoardDto> mbLists = messageBoardService.selectThreadListByTitle(searchText, 0, 20);
		// 書き込み順にスレッドを10件取得
		ArrayList<MessageBoardDto> postCountMbLists = messageBoardService.SelectThreadsOrderPostCount();
		// カテゴリID・nameを取得
		ArrayList<MessageBoardDto> categoryMbLists = messageBoardService.selectCategory();

		mav.addObject("mode", "search");
		mav.addObject("searchText", searchText);
		mav.addObject("mbLists", mbLists);
		mav.addObject("postCountMbLists", postCountMbLists);
		mav.addObject("categoryMbLists", categoryMbLists);
		mav.setViewName("messageBoardList");
	}

	public void showCategoryList(int categoryId, int page, ModelAndView mav) {
		// リクエストパラメータでスレッド名を検索
		Page<MessageBoardDto> mbLists = messageBoardService.selectThreadListByCategoyrId(categoryId, page, 20);
		// 書き込み順にスレッドを10件取得
		ArrayList<MessageBoardDto> postCountMbLists = messageBoardService.SelectThreadsOrderPostCount();
		// カテゴリID・nameを取得
		ArrayList<MessageBoardDto> categoryMbLists = messageBoardService.selectCategory();

		mav.addObject("mode", "category");
		mav.addObject("categoryId", categoryId);
		mav.addObject("mbLists", mbLists);
		mav.addObject("postCountMbLists", postCountMbLists);
		mav.addObject("categoryMbLists", categoryMbLists);
		mav.setViewName("messageBoardList");
	}

	public void showDetail(int threadId, String userId, int page, ModelAndView mav) {
		// threadのidでthread情報を取得
		MessageBoardDto mb = messageBoardService.selectThread(threadId);
		// threadのidでpostをページングで取得
		Page<MessageBoardDetailDto> mdLists = messageBoardService.selectPostsById(threadId, page, 10);
		// postのidとuseridでいいねがあるポストを検索して変数にセット
		mdLists = messageBoardService.getAlreadyLiked(userId, mdLists);
		// 書き込み順にスレッドを10件取得
		ArrayList<MessageBoardDto> postCountMbLists = messageBoardService.SelectThreadsOrderPostCount();
		// カテゴリID・nameを取得
		ArrayList<MessageBoardDto> categoryMbLists = messageBoardService.selectCategory();

		mav.addObject("mb", mb);
		mav.addObject("mdLists", mdLists);
		mav.addObject("postCountMbLists", postCountMbLists);
		mav.addObject("categoryMbLists", categoryMbLists);
		mav.addObject("page", page);
	}
}
