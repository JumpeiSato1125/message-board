package com.example.messageboad.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.messageboad.dao.CategoryDao;
import com.example.messageboad.dao.LikesDao;
import com.example.messageboad.dao.PostsDao;
import com.example.messageboad.dao.ThreadsDao;
import com.example.messageboad.dto.MessageBoardDetailDto;
import com.example.messageboad.dto.MessageBoardDto;
import com.example.messageboad.entities.Category;
import com.example.messageboad.entities.Likes;
import com.example.messageboad.entities.Posts;
import com.example.messageboad.entities.Threads;

@Service
public class MessageBoardService {

	private final ThreadsDao threadsDao;
	private final CategoryDao categoryDao;
	private final PostsDao postsDao;
	private final LikesDao likesDao;

	@Autowired
	public MessageBoardService(ThreadsDao messageBoadDao, CategoryDao categoryDao, PostsDao postsDao,
			LikesDao likesDao) {
		this.threadsDao = messageBoadDao;
		this.categoryDao = categoryDao;
		this.postsDao = postsDao;
		this.likesDao = likesDao;
	}

	// IDでスレッドを一件検索
	public MessageBoardDto selectThread(int threadId) {
		MessageBoardDto mb = new MessageBoardDto();
		Threads thread = threadsDao.findByIdAndDeleted(threadId, 0).orElse(null);
		if (thread != null) {
			mb.setId(thread.getId());
			mb.setTitle(thread.getTitle());
			mb.setThumbnailPath(thread.getThumbnailPath());
			mb.setDescription(thread.getDescription());
			mb.setViewCount(thread.getViewCount());
			mb.setLikeAllCount(thread.getLikeAllCount());
			mb.setCategoryId(thread.getCategoryId());
		}
		return mb;
	}

	// スレッド一覧を取得
	public Page<MessageBoardDto> selectThreadList(int page, int size) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<Threads> threadsLists = threadsDao.findByDeleted(0, pageRequest);
		Page<MessageBoardDto> threadsDtoList = threadsLists.map(threadsList -> {
			MessageBoardDto md = new MessageBoardDto();
			md.setId(threadsList.getId());
			md.setTitle(threadsList.getTitle());
			md.setThumbnailPath(threadsList.getThumbnailPath());
			md.setCreatedAt(threadsList.getCreatedAt().format(formatter));
			md.setViewCount(threadsList.getViewCount());
			md.setLikeAllCount(threadsList.getLikeAllCount());
			return md;
		});
		return threadsDtoList;
	}

	// タイトル名でスレッドを取得
	public Page<MessageBoardDto> selectThreadListByTitle(String title, int page, int size) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").ascending());
		Page<Threads> threadsLists = threadsDao.findByTitleContainingIgnoreCaseAndDeleted(title, 0, pageRequest);
		Page<MessageBoardDto> threadsDtoList = threadsLists.map(threadsList -> {
			MessageBoardDto md = new MessageBoardDto();
			md.setId(threadsList.getId());
			md.setTitle(threadsList.getTitle());
			md.setThumbnailPath(threadsList.getThumbnailPath());
			md.setCreatedAt(threadsList.getCreatedAt().format(formatter));
			md.setViewCount(threadsList.getViewCount());
			md.setLikeAllCount(threadsList.getLikeAllCount());
			return md;
		});
		return threadsDtoList;
	}

	// CategoryIdでスレッドを取得
	public Page<MessageBoardDto> selectThreadListByCategoyrId(int categoryId, int page, int size) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").ascending());
		Page<Threads> threadsLists = threadsDao.findByCategoryIdAndDeleted(categoryId, 0, pageRequest);
		Page<MessageBoardDto> threadsDtoList = threadsLists.map(threadsList -> {
			MessageBoardDto md = new MessageBoardDto();
			md.setId(threadsList.getId());
			md.setTitle(threadsList.getTitle());
			md.setThumbnailPath(threadsList.getThumbnailPath());
			md.setCreatedAt(threadsList.getCreatedAt().format(formatter));
			md.setViewCount(threadsList.getViewCount());
			md.setLikeAllCount(threadsList.getLikeAllCount());
			return md;
		});
		return threadsDtoList;
	}

	// 新規スレッドを６件取得
	public ArrayList<MessageBoardDto> SelectNewThreads() {

		ArrayList<MessageBoardDto> mbList = new ArrayList<MessageBoardDto>();

		List<Threads> ThreadsLists = threadsDao.findTop6ByDeletedOrderByCreatedAtDesc(0);
		for (Threads list : ThreadsLists) {
			MessageBoardDto mb = new MessageBoardDto();
			mb.setId(list.getId());
			mb.setTitle(list.getTitle());
			mb.setThumbnailPath(list.getThumbnailPath());
			mb.setViewCount(list.getViewCount());
			mb.setLikeAllCount(list.getLikeAllCount());
			mbList.add(mb);
		}
		return mbList;
	}

	// 書き込み数順にスレッドを１０件取得
	public ArrayList<MessageBoardDto> SelectThreadsOrderPostCount() {

		ArrayList<MessageBoardDto> mbList = new ArrayList<MessageBoardDto>();

		List<Threads> ThreadsLists = threadsDao.findTop10ByDeletedOrderByPostCountDesc(0);
		for (Threads list : ThreadsLists) {
			MessageBoardDto mb = new MessageBoardDto();
			mb.setId(list.getId());
			mb.setTitle(list.getTitle());
			mb.setViewCount(list.getViewCount());
			mb.setLikeAllCount(list.getLikeAllCount());
			mbList.add(mb);
		}
		return mbList;
	}

	// 人気順にスレッドを10件取得
	public ArrayList<MessageBoardDto> SelectThreadsOrderLikeCount() {

		ArrayList<MessageBoardDto> mbList = new ArrayList<MessageBoardDto>();

		List<Threads> ThreadsLists = threadsDao.findTop10ByDeletedOrderByLikeAllCountDesc(0);
		for (Threads list : ThreadsLists) {
			MessageBoardDto mb = new MessageBoardDto();
			mb.setId(list.getId());
			mb.setTitle(list.getTitle());
			mb.setViewCount(list.getViewCount());
			mb.setLikeAllCount(list.getLikeAllCount());
			mbList.add(mb);
		}
		return mbList;
	}

	// カテゴリを取得
	public ArrayList<MessageBoardDto> selectCategory() {

		ArrayList<MessageBoardDto> mbList = new ArrayList<MessageBoardDto>();

		List<Category> categoryLists = categoryDao.findByDeleted(0);
		for (Category list : categoryLists) {
			MessageBoardDto mb = new MessageBoardDto();
			mb.setCategoryId(list.getCategoryId());
			mb.setCategoryName(list.getCategoryName());
			mbList.add(mb);
		}
		return mbList;
	}

	// ポストを取得
	public Page<MessageBoardDetailDto> selectPostsById(int threadId, int page, int size) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// postテーブルから外部キーでpostを読み取る
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").ascending());
		Page<Posts> postsLists = postsDao.findByThreadIdAndDeleteFlg(threadId, 0, pageRequest);
		Page<MessageBoardDetailDto> postDtos = postsLists.map(postsList -> {
			MessageBoardDetailDto md = new MessageBoardDetailDto();
			md.setPostId(postsList.getId());
			md.setContent(postsList.getContent());
			md.setPostBy(postsList.getPostedBy());
			md.setCreatedAt(postsList.getCreatedAt().format(formatter));
			md.setLikeCount(postsList.getLikeCount());
			return md;
		});
		return postDtos;
	}

	// postのidとuseridでいいねがあるポストを検索し、いいねフラグを変数にセット
	public Page<MessageBoardDetailDto> getAlreadyLiked(String userId, Page<MessageBoardDetailDto> mdDto) {
		return mdDto.map(dto -> {
			boolean liked = likesDao.existsByPostIdAndUserId(dto.getPostId(), userId);
			dto.setAlreadyLiked(liked);
			return dto;
		});
	}

	// スレッドを更新
	public boolean addThread(String title, String description, int categoryId, String thumbnailPath, String userId) {

		try {
			Threads thread = new Threads();
			thread.setTitle(title);
			thread.setThumbnailPath("/images/" + thumbnailPath);
			thread.setCategoryId(categoryId);
			thread.setDescription(description);
			thread.setCreatedBy(userId);
			thread.setCreatedAt(LocalDateTime.now());
			threadsDao.save(thread);
			return true;

		} catch (Exception e) {
			// ログを出力
			e.printStackTrace();
			return false;
		}
	}

	// いいね済みか検索
	public boolean hasLike(int postId, String userId) {
		return likesDao.existsByPostIdAndUserId(postId, userId);
	}

	@Transactional
	public boolean addLike(int threadId, int postId, String userId) {
		try {
			// スレッド取得（存在しない場合は失敗扱い）
			Optional<Threads> thread = threadsDao.findByIdAndDeleted(threadId, 0);
			if (thread.isEmpty()) {
				return false;
			}
			// スレッドテーブルのいいね数をインクリメント
			int threadUpdated = threadsDao.incrementLikeCount(threadId);
			if (threadUpdated != 1) {
				return false;
			}
			// postsテーブルいいね数を1つ増やす
			int postsUpdated = postsDao.incrementLikeCount(postId);
			if (postsUpdated == 1) {
				// いいね記録を保存
				Likes like = new Likes();
				like.setPostId(postId);
				like.setUserId(userId);
				likesDao.save(like);
				return true;
			}
			return false;
		} catch (Exception e) {
			// ログを出力
			e.printStackTrace();
			return false;
		}

	}

	@Transactional
	public boolean addPost(int threadId, String content, String postedBy) {
		try {
			// スレッド取得（存在しない場合は失敗扱い）
			Optional<Threads> thread = threadsDao.findByIdAndDeleted(threadId, 0);
			if (thread.isEmpty()) {
				return false;
			}
			// スレッドテーブルの書き込み数をインクリメント
			int threadUpdated = threadsDao.incrementPostCount(threadId);
			if (threadUpdated != 1) {
				return false;
			}

			Posts post = new Posts();
			post.setThreadId(thread.get().getId());
			post.setContent(content);
			post.setPostedBy(postedBy);
			post.setCreatedAt(LocalDateTime.now());
			post.setDeleteFlg(0);
			post.setLikeCount(0);
			postsDao.save(post);
			return true;

		} catch (Exception e) {
			// ログを出力
			e.printStackTrace();
			return false;
		}
	}
}
