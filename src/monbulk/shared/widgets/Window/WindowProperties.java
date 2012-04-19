package monbulk.shared.widgets.Window;
/*
 * Class: WindowProperties
 * Author: Andrew Glenn
 * Date: 25.3.12
 * Description: Really just a single data type for Window properties that are shared across platforms
 */
public class WindowProperties {

	private int _width;
	private int _height;
	private Boolean _canMove;
	private Boolean _canResize;
	private Boolean _canExpand;
	private Boolean _canShrink;
	private Boolean _isModal;
	private String _WindowStyleName;
	private String _WindowType;
	private String _WindowName;
	private int _x;
	private int _y;
	/*
	 * Default constructor
	 */
	public WindowProperties()
	{
		//default values
		_width=500;
		this._height=500;
		this._canShrink = false;
		this._canMove = false;
		this._canExpand = false;
		this._isModal = false;
		this._WindowStyleName ="";
		this._WindowType ="DEFAULT";
		this._x=0;
		this._y=0;
		this._WindowName="Unknown";
	}
	/*
	 * 
	 */
	public int get_width() {
		return _width;
	}
	public void set_width(int _width) {
		this._width = _width;
	}
	public int get_height() {
		return _height;
	}
	public void set_height(int _height) {
		this._height = _height;
	}
	public Boolean get_canMove() {
		return _canMove;
	}
	public void set_canMove(Boolean _canMove) {
		this._canMove = _canMove;
	}
	public Boolean get_canExpand() {
		return _canExpand;
	}
	public void set_canExpand(Boolean _canExpand) {
		this._canExpand = _canExpand;
	}
	public Boolean get_canShrink() {
		return _canShrink;
	}
	public void set_canShrink(Boolean _canShrink) {
		this._canShrink = _canShrink;
	}
	public Boolean get_isModal() {
		return _isModal;
	}
	public void set_isModal(Boolean _isModal) {
		this._isModal = _isModal;
	}
	public String get_WindowStyleName() {
		return _WindowStyleName;
	}
	public void set_WindowStyleName(String _WindowStyleName) {
		this._WindowStyleName = _WindowStyleName;
	}
	public String get_WindowType() {
		return _WindowType;
	}
	public void set_WindowType(String _WindowType) {
		this._WindowType = _WindowType;
	}
	public Boolean get_canResize() {
		return _canResize;
	}
	public void set_canResize(Boolean _canResize) {
		this._canResize = _canResize;
	}
	public int get_x() {
		return _x;
	}
	public void set_x(int _x) {
		this._x = _x;
	}
	public int get_y() {
		return _y;
	}
	public void set_y(int _y) {
		this._y = _y;
	}
	public String get_WindowName() {
		return _WindowName;
	}
	public void set_WindowName(String _WindowName) {
		this._WindowName = _WindowName;
	}
}
