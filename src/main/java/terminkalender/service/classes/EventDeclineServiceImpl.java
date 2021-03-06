package terminkalender.service.classes;

import terminkalender.builders.DAOObjectBuilder;
import terminkalender.dao.interfaces.EventDeclineDAO;
import terminkalender.dao.interfaces.UserDAO;
import terminkalender.exceptions.ObjectIstNullException;
import terminkalender.model.interfaces.EventDecline;
import terminkalender.model.interfaces.User;
import terminkalender.service.interfaces.EventDeclineService;
import terminkalender.util.util;
import terminkalender.validators.ObjectValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Resource / Service class for Event Decline - Object
 *
 * @author Piri, Shenna RWP
 * @author Bimantara, Agra
 */
@Path(EventDeclineServiceImpl.webContextPath)
public class EventDeclineServiceImpl implements EventDeclineService
{
    private EventDeclineDAO eventDeclineDAO;
    private UserDAO userDAO;
    static final String webContextPath = "decline";

    /**
     * constructor, the DAOs is passed as parameters
     * @param eventDeclineDAO DAO for EventDecline
     * @param userDAO DAO for User
     * @throws ObjectIstNullException if there is DAO Object that has not been instantiated
     */
    public EventDeclineServiceImpl (EventDeclineDAO eventDeclineDAO, UserDAO userDAO) throws ObjectIstNullException {
        ObjectValidator.checkObObjectNullIst(eventDeclineDAO);
        ObjectValidator.checkObObjectNullIst(userDAO);
        this.eventDeclineDAO = eventDeclineDAO;
        this.userDAO = userDAO;
    }

    /**
     * standard constructor for generating the DAOs with the builder
     * @throws ObjectIstNullException when some object is null
     */
    public EventDeclineServiceImpl() throws  ObjectIstNullException{
        this (DAOObjectBuilder.getEventDeclineDaoObject(), DAOObjectBuilder.getUserDaoObject());
    }

    //ex: localhost:8000/decline/{request body containing the new decline object}
    /** -------------------------------- POST --------------------------------
     * POST-endpoint for creating new Event Decline
     * request body should contain event Event Decline object WITHOUT the id
     * @param eventDecline the new Event Decline object want to be added
     * @return the new Event Decline after stored in the database and given id
     */
    @Override
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EventDecline addDecline (EventDecline eventDecline) {
        int newDeclineId = eventDeclineDAO.addEventDecline(eventDecline);
        return  eventDeclineDAO.getEventDecline(newDeclineId);
    }

    //ex: localhost:8000/decline/{declineid}
    /** -------------------------------- GET --------------------------------
     * GET-endpoint for retrieving 1 Event Decline
     * @param declineId the id of the Event Decline wants to be retrieved
     * @return the Event Decline having the declineId
     */
    @Override
    @GET
    @Path("{declineid}")
    @Produces(MediaType.APPLICATION_JSON)
    public EventDecline getDecline(@PathParam("declineid") int declineId) {
        return eventDeclineDAO.getEventDecline(declineId);
    }

    //ex: localhost:8000/decline/event/{eventid}
    /** ------------- GET ALL USER WHO DECLINE ------------------------------
     * GET-endpoint for retrieving all user who decline the event
     * @param eventId id of the event
     * @return all user who decline the event
     */
    @Override
    @GET
    @Path("event/{eventid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserWhoDecline(@PathParam("eventid") int eventId) {
        String result = "";
        List<Integer> listOfUserId = eventDeclineDAO.getUserWhoDecline(eventId);
        List<User> userList = listOfUserId.stream()
                                          .map(userDAO::getUserById)
                                          .collect(Collectors.toList());
        return util.convertUserListToNameList(userList);
    }

    //ex: localhost:8000/decline/{declineid}
    /** -------------------------------- DELETE --------------------------------
     * DELETE-endpoint for deleting Event Decline
     * @param declineId the id of the Event Decline wants to be deleted
     */
    @Override
    @DELETE
    @Path("{declineid}")
    public void deleteDecline(@PathParam("declineid") int declineId) {
        eventDeclineDAO.deleteEventDecline(declineId);
    }
}
