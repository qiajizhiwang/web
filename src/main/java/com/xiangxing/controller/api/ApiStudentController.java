package com.xiangxing.controller.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.MessageMapper;
import com.xiangxing.mapper.MessageQueueMapper;
import com.xiangxing.mapper.NoticeDetailMapper;
import com.xiangxing.mapper.SchoolImageMapper;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.mapper.StudentHomeworkMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.mapper.ex.CourseMapperEx;
import com.xiangxing.mapper.ex.CourseSignPoMapper;
import com.xiangxing.mapper.ex.EntryFormPoMapper;
import com.xiangxing.mapper.ex.HomeworkPoMapper;
import com.xiangxing.mapper.ex.MessageQueuePoMapper;
import com.xiangxing.mapper.ex.NoticePoMapper;
import com.xiangxing.mapper.ex.ProductPoMapper;
import com.xiangxing.mapper.ex.StudentPoMapper;
import com.xiangxing.model.Message;
import com.xiangxing.model.MessageExample;
import com.xiangxing.model.MessageQueue;
import com.xiangxing.model.MessageQueueExample;
import com.xiangxing.model.NoticeDetail;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolImage;
import com.xiangxing.model.SchoolImageExample;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentHomework;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.model.ex.CourseSignPo;
import com.xiangxing.model.ex.EntryFormPo;
import com.xiangxing.model.ex.HomeworkPo;
import com.xiangxing.model.ex.MessageQueuePo;
import com.xiangxing.model.ex.NoticePo;
import com.xiangxing.model.ex.ProductPo;
import com.xiangxing.model.ex.StudentPo;
import com.xiangxing.utils.DateUtil;
import com.xiangxing.vo.api.ApiPageResponse;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.CourseSignResponse;
import com.xiangxing.vo.api.LoginInfo;
import com.xiangxing.vo.api.SchoolResponse;
import com.xiangxing.vo.api.StudentVo;

@RequestMapping("/api/student")
@RestController
public class ApiStudentController {

	@Value(value = "${homework_path}")
	private String imagePath;

	@Value(value = "${message_path}")
	private String messagePath;

	@Autowired
	private TeacherMapper teacherMapper;

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	CourseMapperEx courseMapperEx;

	@Autowired
	ProductPoMapper productPoMapper;

	@Autowired
	HomeworkPoMapper homeworkPoMapper;

	@Autowired
	StudentHomeworkMapper studentHomeworkMapper;

	@Autowired
	private EntryFormPoMapper entryFormPoMapper;

	@Autowired
	private CourseSignPoMapper courseSignPoMapper;

	/**
	 * 获取学生签到信息
	 * 
	 * @param courseId
	 * @param students
	 * @return
	 */
	@RequestMapping("/getCourseSignInfo")
	public ApiResponse getCourseSignInfo(Long courseId, Long singTime) {
		LoginInfo info = TokenManager.getNowUser();
		Date singDate = null;
		if (null != singTime && 0 != singTime) {
			singDate = DateUtil.stringToDate(DateUtil.dateToString(new Date(singTime), DateUtil.patternA));
		}
		List<CourseSignPo> courseSignPos = courseSignPoMapper.getCourseSignInfo(courseId, info.getId(), singDate);
		CourseSignResponse courseSignResponse = new CourseSignResponse();
		courseSignResponse.setCourseSignPos(courseSignPos);
		return courseSignResponse;

	}

	@RequestMapping("/myCourses")
	public ApiResponse myCourses(PageRequest pageRequest, HttpServletRequest httpServletRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List<CourseEx> courses = courseMapperEx.courseListByStudentId(info.getId());
		for (CourseEx course : courses) {
			course.setImageUrl(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + course.getImageUrl());
		}
		long total = page.getTotal();
		return new ApiPageResponse<CourseEx>(total, courses);

	}

	@RequestMapping("/myProducts")
	public ApiResponse myProducts(PageRequest pageRequest, HttpServletRequest httpServletRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List<ProductPo> products = productPoMapper.getListByStudentId(info.getId());
		for (ProductPo product : products) {
			product.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + product.getPath());
		}
		long total = page.getTotal();
		return new ApiPageResponse<ProductPo>(total, products);

	}

	@Autowired
	SchoolMapper schoolMapper;

	@Autowired
	SchoolImageMapper schoolImageMapper;
	@Autowired
	private StudentPoMapper studentPoMapper;

	@RequestMapping("/mySchool")
	public ApiResponse mySchool(HttpServletRequest httpServletRequest) {
		LoginInfo info = TokenManager.getNowUser();

		School school = schoolMapper.selectByPrimaryKey(studentMapper.selectByPrimaryKey(info.getId()).getSchoolId());
		SchoolResponse response = new SchoolResponse();
		try {
			BeanUtils.copyProperties(response, school);
			SchoolImageExample schoolImageExample = new SchoolImageExample();
			schoolImageExample.createCriteria().andSchoolIdEqualTo(response.getId());
			schoolImageExample.setOrderByClause("sort");
			List<SchoolImage> list = schoolImageMapper.selectByExample(schoolImageExample);
			List<String> paths = new ArrayList<>();
			for (SchoolImage schoolImage : list) {
				paths.add(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + schoolImage.getPath());
			}
			response.setPaths(paths);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}

	@RequestMapping("/myInfo")
	public ApiResponse myInfo() {
		LoginInfo info = TokenManager.getNowUser();
//		Student student = studentMapper.selectByPrimaryKey(info.getId());
		
		List<StudentPo> students = studentPoMapper.list(null, null,info.getId());
		
		StudentVo s = new StudentVo();
		try {
			BeanUtils.copyProperties(s, students.get(0));
//			BeanUtils.copyProperties(s, student);
//			s.setSchoolName(schoolMapper.selectByPrimaryKey(s.getSchoolId()).getName());
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	@RequestMapping("/myHomeworks")
	public ApiResponse myHomeworks(PageRequest pageRequest, Long courseId, Integer status,
			HttpServletRequest httpServletRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List<HomeworkPo> homeworks = homeworkPoMapper.list(info.getId(), courseId, status);
		for (HomeworkPo homeworkPo : homeworks) {
			homeworkPo.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + homeworkPo.getPath());
			homeworkPo.setJobPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + homeworkPo.getJobPath());

		}
		long total = page.getTotal();
		return new ApiPageResponse<HomeworkPo>(total, homeworks);

	}

	@RequestMapping("/uploadHomework")
	public ApiResponse uploadHomework(Long id, String base64Image) {
		LoginInfo info = TokenManager.getNowUser();

		byte[] imageByte = Base64Utils.decodeFromString(base64Image);
		BufferedImage image;
		FileOutputStream fileOutputStream = null;
		try {
			String dir = imagePath + File.separator + info.getId();
			if (!new File(dir).exists())
				new File(dir).mkdirs();
			String path = dir + File.separator + id + ".jpg";
			fileOutputStream = new FileOutputStream(path);
			image = ImageIO.read(new ByteArrayInputStream(imageByte));
			int srcWidth = image.getWidth(); // 源图宽度
			int srcHeight = image.getHeight(); // 源图高度
			ImageIO.write(image, "jpg", fileOutputStream);
			StudentHomework studentHomework = studentHomeworkMapper.selectByPrimaryKey(id);
			studentHomework.setWidth(srcWidth);
			studentHomework.setHeight(srcHeight);
			studentHomework.setPath(path);
			studentHomework.setFinishTime(new Date());
			studentHomework.setStatus(2);
			studentHomeworkMapper.updateByPrimaryKeySelective(studentHomework);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != fileOutputStream)
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return new ApiResponse();

	}

	@Autowired
	NoticePoMapper noticePoMapper;

	@RequestMapping("/myNotices")
	public ApiResponse myNotices(PageRequest pageRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List notices = noticePoMapper.list(info.getId());
		long total = page.getTotal();
		return new ApiPageResponse<NoticePo>(total, notices);

	}

	@Autowired
	NoticeDetailMapper noticeDetailMapper;

	@RequestMapping("/readNotice")
	public ApiResponse readNotice(Long noticeId) {
		LoginInfo info = TokenManager.getNowUser();

		NoticeDetail noticeDetail = noticeDetailMapper.selectByPrimaryKey(noticeId);
		noticeDetail.setReadTime(new Date());
		noticeDetail.setStatus(2);
		noticeDetailMapper.updateByPrimaryKey(noticeDetail);
		return new ApiResponse();

	}

	@Autowired
	MessageMapper messageMapper;

	@Autowired
	MessageQueueMapper messageQueueMapper;

	@RequestMapping("/getMessages")
	public ApiPageResponse<Message> getMessages(PageRequest pageRequest, Long teacherId,
			HttpServletRequest httpServletRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		MessageQueueExample messageQueueExample = new MessageQueueExample();
		messageQueueExample.createCriteria().andStudentEqualTo(info.getId()).andTeacherEqualTo(teacherId);
		List<MessageQueue> messageQueues = messageQueueMapper.selectByExample(messageQueueExample);
		MessageQueue messageQueue = null;
		if (CollectionUtils.isEmpty(messageQueues)) {
			messageQueue = new MessageQueue();
			messageQueue.setStudent(info.getId());
			messageQueue.setTeacher(teacherId);
			messageQueueMapper.insert(messageQueue);
		} else {
			messageQueue = messageQueues.get(0);
		}
		MessageExample messageExample = new MessageExample();
		messageExample.createCriteria().andQueueIdEqualTo(messageQueue.getId());
		messageExample.setOrderByClause("id desc");
		List<Message> messages = messageMapper.selectByExample(messageExample);
		for (Message message : messages) {
			if(message.getType() ==2 )
			message.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + message.getPath());
		}
		long total = page.getTotal();
		return new ApiPageResponse<Message>(total, messages);
	}

	@RequestMapping("/sendMessage")
	public ApiResponse sendMessage(String text, Long teacherId, String base64Image) {
		LoginInfo info = TokenManager.getNowUser();
		MessageQueueExample messageQueueExample = new MessageQueueExample();
		messageQueueExample.createCriteria().andStudentEqualTo(info.getId()).andTeacherEqualTo(teacherId);
		List<MessageQueue> messageQueues = messageQueueMapper.selectByExample(messageQueueExample);
		MessageQueue messageQueue = null;
		if (CollectionUtils.isEmpty(messageQueues)) {
			messageQueue = new MessageQueue();
			messageQueue.setStudent(info.getId());
			messageQueue.setTeacher(teacherId);
			messageQueueMapper.insert(messageQueue);
		} else {
			messageQueue = messageQueues.get(0);
		}
		Message message = new Message();
		message.setCreateTime(new Date());
		message.setOrigin(2);
		message.setQueueId(messageQueue.getId());
		if (StringUtils.isEmpty(base64Image)) {
			message.setType(1);
			message.setText(text);
		} else {
			byte[] imageByte = Base64Utils.decodeFromString(base64Image);
			BufferedImage image;
			FileOutputStream fileOutputStream = null;
			try {
				String dir = messagePath + File.separator + messageQueue.getId();
				if (!new File(dir).exists())
					new File(dir).mkdirs();
				String path = dir + File.separator + UUID.randomUUID() + ".png";
				fileOutputStream = new FileOutputStream(path);
				image = ImageIO.read(new ByteArrayInputStream(imageByte));
				ImageIO.write(image, "png", fileOutputStream);
				message.setType(2);
				message.setPath(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (null != fileOutputStream)
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		}
		messageMapper.insert(message);
		return new ApiResponse();

	}

	/**
	 * 获取学生报名信息
	 * 
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/entryFormList")
	@ResponseBody
	public ApiResponse entryFormList(PageRequest pageRequest) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		LoginInfo info = TokenManager.getNowUser();
		List<EntryFormPo> entryFormPos = entryFormPoMapper.list(null, null, null, info.getId());
		long total = page.getTotal();
		return new ApiPageResponse<EntryFormPo>(total, entryFormPos);

	}

	@Autowired
	MessageQueuePoMapper messageQueuePoMapper;

	@RequestMapping("/getQueues")
	public ApiPageResponse<MessageQueuePo> getQueues(PageRequest pageRequest, HttpServletRequest httpServletRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		List<MessageQueuePo> messages = messageQueuePoMapper.list(null, info.getId(),null);
		long total = page.getTotal();
		return new ApiPageResponse<MessageQueuePo>(total, messages);
	}
}
