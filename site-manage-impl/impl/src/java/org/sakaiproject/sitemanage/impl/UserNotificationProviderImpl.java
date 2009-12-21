package org.sakaiproject.sitemanage.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.email.api.EmailService;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.sitemanage.api.UserNotificationProvider;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.util.ResourceLoader;




public class UserNotificationProviderImpl implements UserNotificationProvider {
	
	private static Log M_log = LogFactory.getLog(UserNotificationProviderImpl.class);
	private EmailService emailService; 
	
	public void setEmailService(EmailService es) {
		emailService = es;
	}
	
	private ServerConfigurationService serverConfigurationService;
	public void setServerConfigurationService(ServerConfigurationService scs) {
		serverConfigurationService = scs;
	}
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		userDirectoryService = uds;
	}
	
	/** portlet configuration parameter values* */
	/** Resource bundle using current language locale */
	//private static ResourceLoader rb = new ResourceLoader("UserNotificationProvider");

	public void init() {
		//nothing realy to do
		M_log.info("init()");
	}
	
	public void notifyAddedParticipant(boolean nonOfficialAccount,
			User user, Site site) {
		ResourceLoader rb = new ResourceLoader(user.getId(), "UserNotificationProvider");
		
		String from = serverConfigurationService.getBoolean(NOTIFY_FROM_CURRENT_USER, false)?
				getCurrentUserEmailAddress():getSetupRequestEmailAddress();
		if (from != null) {
			String productionSiteName = serverConfigurationService.getString(
					"ui.service", "");
			String productionSiteUrl = serverConfigurationService
					.getPortalUrl();
			String nonOfficialAccountUrl = serverConfigurationService.getString(
					"nonOfficialAccount.url", null);
			String emailId = user.getEmail();
			String to = emailId;
			String headerTo = emailId;
			String replyTo = emailId;
			String message_subject = productionSiteName + " "
					+ rb.getString("java.sitenoti");
			String content = "";
			StringBuilder buf = new StringBuilder();
			buf.setLength(0);

			// email bnonOfficialAccounteen newly added nonOfficialAccount account
			// and other users
			buf.append(user.getDisplayName() + ":\n\n");
			buf.append(rb.getFormattedMessage("java.addedsite", new Object[]{productionSiteName, site.getTitle(), userDirectoryService.getCurrentUser().getDisplayName()}) + "\n");
			if (nonOfficialAccount) {
				buf.append(serverConfigurationService.getString(
						"nonOfficialAccountInstru", "")
						+ "\n");

				if (nonOfficialAccountUrl != null) {
					buf.append(rb.getString("java.togeta1") + "\n"
							+ nonOfficialAccountUrl + "\n");
					buf.append(rb.getString("java.togeta2") + "\n\n");
				}
				buf.append(rb.getFormattedMessage("java.tolog", new Object[] {
						site.getUrl(),
						serverConfigurationService.getString("xlogin.text", "Login"),
						site.getTitle()
						}));
			} else {
				buf.append(rb.getFormattedMessage("java.tolog", new Object[] {
						site.getUrl(),
						serverConfigurationService.getString("login.text", "Login"),
						site.getTitle()
						}));
			}
			content = buf.toString();
			emailService.send(from, to, message_subject, content, headerTo,
					replyTo, null);

		} // if

	}

	public void notifyNewUserEmail(User user, String newUserPassword,
			Site site) {
		ResourceLoader rb = new ResourceLoader("UserNotificationProvider");
		// set the locale to individual receipient's setting
		rb.setContextLocale(rb.getLocale(user.getId()));
		
		String from = getSetupRequestEmailAddress();
		String productionSiteName = serverConfigurationService.getString(
				"ui.service", "");
		String productionSiteUrl = serverConfigurationService.getPortalUrl();
		
		String newUserEmail = user.getEmail();
		String to = newUserEmail;
		String headerTo = newUserEmail;
		String replyTo = newUserEmail;
		String message_subject = rb.getFormattedMessage("java.newusernoti",
				new Object[]{productionSiteName});
		String content = "";

		if (from != null && newUserEmail != null) {
			StringBuilder buf = new StringBuilder();
			buf.setLength(0);

			// email body
			buf.append(user.getDisplayName() + ":\n\n");

			buf.append(rb.getFormattedMessage("java.addedto", new Object[]{
					productionSiteName,
					productionSiteUrl,
					userDirectoryService.getCurrentUser().getDisplayName()
					})).append("\n\n");
			buf.append(rb.getFormattedMessage("java.usernamedis", new Object[]{user.getEid()})).append('\n');
			buf.append(rb.getFormattedMessage("java.passwordis", new Object[]{newUserPassword})).append('\n');;
			buf.append(rb.getString("java.newuserfooter"));
			
			content = buf.toString();
			emailService.send(from, to, message_subject, content, headerTo,
					replyTo, null);
		}
	}

	/*
	 *  Private methods
	 */
	private String getCurrentUserEmailAddress() {
		User currentUser = userDirectoryService.getCurrentUser();
		String email = currentUser != null ? currentUser.getEmail():null;
		if (email == null || email.length() == 0) {
			email = getSetupRequestEmailAddress();
		}
		return email;
	}
	
	
	private String getSetupRequestEmailAddress() {
		String from = serverConfigurationService.getString("setup.request",
				null);
		if (from == null) {
			from = "postmaster@".concat(serverConfigurationService
					.getServerName());
			M_log.warn(this + " - no 'setup.request' in configuration, using: "+ from);
		}
		return from;
	}
}
