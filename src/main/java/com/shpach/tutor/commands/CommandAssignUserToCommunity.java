package com.shpach.tutor.commands;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.service.CommunityService;
import com.shpach.tutor.service.SessionServise;

/**
 * Command which assign user to community
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandAssignUserToCommunity implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandAssignUserToCommunity.class);
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
		String communityId = request.getParameter("communityId");
		String userName = request.getParameter("userName");

		boolean isOk = CommunityService.getInstance().assignUserToCommunity(userName, communityId);
		request.setAttribute("assignUserStatus", isOk);

		page = "/pages";
		return page;
	}

}
