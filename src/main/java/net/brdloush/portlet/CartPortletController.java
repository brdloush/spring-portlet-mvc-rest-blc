package net.brdloush.portlet;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;

import net.brdloush.portlet.attr.UserInfo;
import net.brdloush.portlet.config.CartPortletConfig;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * Handles requests for the portlet view mode.
 */
@Controller
@RequestMapping({ "VIEW", "EDIT" })
@SessionAttributes("currentUserInfo")
public class CartPortletController {

	private static final Logger logger = LoggerFactory
			.getLogger(CartPortletController.class);

	private Client jerseyJSONClient = null;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping("VIEW")
	public String home(
			@ModelAttribute("currentUserInfo") UserInfo currentUserInfo,
			Locale locale, RenderResponse response, Model model) {
	
		StopWatch sw = new StopWatch("home");
		sw.start("addCommonAttributes");

		if (logger.isDebugEnabled()) {
			logger.debug("Welcome home! the client locale is " + locale.toString());
		}
		addCommonAttributes(model, locale, response, currentUserInfo);

		sw.stop();
		
		
		OrderWrapper order = null;

		// there's a chance that ACTION phase already obtained current order
		if (model.containsAttribute("order")) {
			order = (OrderWrapper) model.asMap().get("order");
		}

		
		if (order == null) {
			sw.start("obtain order");
			order = obtainCurrentOrder(currentUserInfo);
			
			if (logger.isDebugEnabled()) {
				logger.debug("adding order to model : "+order);
			}
			model.addAttribute("order",order);
			sw.stop();
		}
		
		if (order != null) {
			List<OrderItemWrapper> orderItems = order.getOrderItems();
			model.addAttribute("orderItemsCount", CollectionUtils.isEmpty(orderItems) ? 0 : orderItems.size());
			
		}

		addStopWatchToModel(model, sw);

		return "home";
	}

	@ActionMapping("addToOrderAction")
	public void addToOrderAction(@ModelAttribute("currentUserInfo") UserInfo currentUserInfo, @RequestParam String skuId, Locale locale, PortletResponse response, Model model) {
		StopWatch sw = new StopWatch("addToOrderAction");
		sw.start("req/res");
		
		if (logger.isDebugEnabled()) {
			logger.debug("addToOrderAction, skuId = "+skuId);
		}
		 
		Client client = getConfiguredJerseyClient();
		WebResource cartResource = client.resource("http://localhost:8080/api/v1/").path("cart/"+skuId).queryParam("customerId", currentUserInfo.getCustomerId().toString());

		ClientResponse clientResp = cartResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			   .post(ClientResponse.class, null);	   

		sw.stop();

		sw.start("interpret res");
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("- response received, statusCode =  "+clientResp.getStatusInfo().getStatusCode());
		}
	
		if (clientResp.getStatusInfo().getFamily() == Family.SUCCESSFUL) { 
			OrderWrapper order = clientResp.getEntity(new com.sun.jersey.api.client.GenericType<OrderWrapper>() {});
			currentUserInfo.setCustomerId(order.getCustomer().getId());
			model.addAttribute("order", order);
		}
		sw.stop();
		addStopWatchToModel(model, sw);
	}
	
	@ActionMapping("removeItemAction")
	public void removeItemAction(@ModelAttribute("currentUserInfo") UserInfo currentUserInfo, @RequestParam String itemId, Locale locale, PortletResponse response, Model model) {
		StopWatch sw = new StopWatch("removeItemAction");
		
		sw.start("req/res");
		
		if (logger.isDebugEnabled()) {
			logger.debug("removeItemAction, itemId = "+itemId);
		}
		 
		Client client = getConfiguredJerseyClient();
		WebResource cartResource = client.resource("http://localhost:8080/api/v1/").path("cart/items/"+itemId).queryParam("customerId", currentUserInfo.getCustomerId().toString());

		ClientResponse clientResp = cartResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			   .delete(ClientResponse.class, null);	   

		sw.stop();
		
		sw.start("interpret-res");
		if (logger.isDebugEnabled()) {
			logger.debug("- response received, statusCode =  "+clientResp.getStatusInfo().getStatusCode());
		}
	
		if (clientResp.getStatusInfo().getFamily() == Family.SUCCESSFUL) { 
			OrderWrapper order = clientResp.getEntity(new com.sun.jersey.api.client.GenericType<OrderWrapper>() {});
			currentUserInfo.setCustomerId(order.getCustomer().getId());
			model.addAttribute("order", order);
		}
		sw.stop();
		
		addStopWatchToModel(model, sw);

	}

	private void addStopWatchToModel(Model model, StopWatch sw) {
		List<StopWatch> stopWatches = getStopWatches(model);
		stopWatches.add(sw);
	}

	@SuppressWarnings("unchecked")
	private List<StopWatch> getStopWatches(Model model) {
		List<StopWatch> stopWatches = null;
		if (!model.containsAttribute("stopWatches")) {
			stopWatches = new LinkedList<StopWatch>();
			model.addAttribute("stopWatches", stopWatches);
		} else {
			stopWatches = (List<StopWatch>) model.asMap().get("stopWatches");
		}	
		return stopWatches;
	}

	@ActionMapping("sayHelloAction")
	public void sayHelloAction(@ModelAttribute("currentUserInfo") UserInfo currentUserInfo, @RequestParam String username, Locale locale, PortletResponse response, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("sayHelloAction, username = "+username);
		}

		model.addAttribute("username", username);
	}

	private OrderWrapper obtainCurrentOrder(UserInfo currentUserInfo) {
		Client client = getConfiguredJerseyClient();

		OrderWrapper order = null;
		WebResource cartResource = client.resource("http://localhost:8080/api/v1/").path("cart/");

		// if we don't have user, create new cart 
		if (currentUserInfo.getCustomerId() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("going to ask for new cart + customer");
			}
			
			ClientResponse clientResp = cartResource.type(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
					   .post(ClientResponse.class, null);	   
			
			if (logger.isDebugEnabled()) {
				logger.debug("- response received, statusCode =  "+clientResp.getStatusInfo().getStatusCode());
			}
			
			if (clientResp.getStatusInfo().getFamily() == Family.SUCCESSFUL) { 
				order = clientResp.getEntity(new com.sun.jersey.api.client.GenericType<OrderWrapper>() {});
				currentUserInfo.setCustomerId(order.getCustomer().getId());
			}
		} else {
			// we have user -> get his cart
			Long customerId = currentUserInfo.getCustomerId();
			if (logger.isDebugEnabled()) {
				logger.debug("going to ask for existing cart of customer " +customerId);
			}
			WebResource existingCartResource = cartResource.queryParam("customerId", customerId.toString());

			ClientResponse clientResp = existingCartResource.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				   .get(ClientResponse.class);	
			
			if (logger.isDebugEnabled()) {
				logger.debug("- response received, statusCode =  "+clientResp.getStatusInfo().getStatusCode());
			}				
			if (clientResp.getStatusInfo().getFamily() == Family.SUCCESSFUL) { 
				order = clientResp.getEntity(new com.sun.jersey.api.client.GenericType<OrderWrapper>() {});

			}		
		}
		return order;
	}

	private Client getConfiguredJerseyClient() {
		if (jerseyJSONClient == null) {
			ClientConfig cfg = new DefaultClientConfig();
			cfg.getClasses().add(JacksonJsonProvider.class);
			jerseyJSONClient = Client.create(cfg);
		}
		
		return jerseyJSONClient;
	}

	@RequestMapping("EDIT")
	public String edit(
			@ModelAttribute("currentUserInfo") UserInfo currentUserInfo,
			Locale locale, Model model, RenderRequest request,
			RenderResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("EDIT");
		}
		addCommonAttributes(model, locale, response, currentUserInfo);
		return "home";
	}

	private void addCommonAttributes(Model model, Locale locale,
			RenderResponse response, UserInfo currentUserInfo) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("sayHelloAction", createActionUrl("sayHelloAction", response));
		model.addAttribute("addToOrderAction", createActionUrl("addToOrderAction", response));
		model.addAttribute("removeItemAction", createActionUrl("removeItemAction", response));

	}

	private Object createActionUrl(String action, RenderResponse response) {
		PortletURL actionUrl = response.createActionURL();
		actionUrl.setParameter(ActionRequest.ACTION_NAME, action);
		return actionUrl;
	}

	@ModelAttribute(value = "currentUserInfo")
	public UserInfo getUserInfo(PortletRequest request) {
		return new UserInfo();
	}

	@ModelAttribute(value = "cartPortletConfig")
	public CartPortletConfig getCartPortletConfig(PortletRequest request) {
		// fixme read config values off request
		return null;
	}
}
