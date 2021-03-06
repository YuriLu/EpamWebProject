package com.shpach.tutor.commands;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.CommunityService;
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.UserService;

/**
 * Command which add new Community to database
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandNewCommunity implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandNewCommunity.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = null;
		boolean checkSession = false;

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.warn("try to access without session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		checkSession = SessionServise.getInstance().checkSession(session.getId(), (String) session.getAttribute("user"));
		if (!checkSession) {
			session.invalidate();
			logger.warn("invalid session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		User user = UserService.getInstance().getUserByLogin((String) session.getAttribute("user"));

		String categoryName = request.getParameter("comm_name");
		Community community = new Community();
		community.setCommunityName(categoryName);
		community.setCommunityActive((byte) 1);
		boolean isOk = CommunityService.getInstance().addNewCommunity(community, user);
		request.setAttribute("addCommunityStatus", isOk);

		page = "/pages";
		return page;
	}

}
