package chat.web.model;

/**
 * This class contains information pertaining to the status of an operation.
 *
 * @author Bryan Daniel
 */
public class ProcessingStatus {

    /**
     * The constant indicating a success status
     */
    public static final String SUCCESS = "success";

    /**
     * The constant indicating an error status
     */
    public static final String ERROR = "error";

    /**
     * The status of the operation
     */
    private String status;

    /**
     * The detailed message
     */
    private String details;

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the value of details
     *
     * @return the value of details
     */
    public String getDetails() {
        return details;
    }

    /**
     * Set the value of details
     *
     * @param details new value of details
     */
    public void setDetails(String details) {
        this.details = details;
    }
}
