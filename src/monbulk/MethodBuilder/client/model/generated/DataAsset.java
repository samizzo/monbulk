
package daris.Monbulk.MethodBuilder.client.model.generated;



import com.google.gwt.user.client.rpc.IsSerializable;



/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="http://www.generated.model.client.MethodBuilder.Monbulk.versi.edu" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="DataAsset">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="ID" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="Title"/>
 *     &lt;xs:element type="xs:string" name="Author"/>
 *     &lt;xs:element type="xs:string" name="Description"/>
 *     &lt;xs:element type="xs:string" name="DataUsage"/>
 *     &lt;xs:element type="xs:string" name="DateCreated"/>
 *     &lt;xs:element type="xs:string" name="Keywords"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class DataAsset implements IsSerializable
{
    /**
	 * 
	 */
	
	
	private String ID;
    private String title;
    private String author;
    private String description;
    private String dataUsage;
    private String dateCreated;
    private String keywords;

    /** 
     * Get the 'ID' element value.
     * 
     * @return value
     */
    public String getID() {
        return ID;
    }
    
    /** 
     * Set the 'ID' element value.
     * 
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /** 
     * Get the 'Title' element value.
     * 
     * @return value
     */
    public String getTitle() {
        return title;
    }

    /** 
     * Set the 'Title' element value.
     * 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /** 
     * Get the 'Author' element value.
     * 
     * @return value
     */
    public String getAuthor() {
        return author;
    }

    /** 
     * Set the 'Author' element value.
     * 
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /** 
     * Get the 'Description' element value.
     * 
     * @return value
     */
    public String getDescription() {
        return description;
    }

    /** 
     * Set the 'Description' element value.
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     * Get the 'DataUsage' element value.
     * 
     * @return value
     */
    public String getDataUsage() {
        return dataUsage;
    }

    /** 
     * Set the 'DataUsage' element value.
     * 
     * @param dataUsage
     */
    public void setDataUsage(String dataUsage) {
        this.dataUsage = dataUsage;
    }

    /** 
     * Get the 'DateCreated' element value.
     * 
     * @return value
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /** 
     * Set the 'DateCreated' element value.
     * 
     * @param dateCreated
     */
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /** 
     * Get the 'Keywords' element value.
     * 
     * @return value
     */
    public String getKeywords() {
        return keywords;
    }

    /** 
     * Set the 'Keywords' element value.
     * 
     * @param keywords
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
