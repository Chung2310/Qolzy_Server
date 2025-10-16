package com.example.Qolzy.model;

public class Messages {
    private Messages() {} // chặn khởi tạo

    // Thông báo chung cho bài viết
    public static final String POST_NOT_FOUND = "Không tìm thấy bài viết";
    public static final String POST_CREATED = "Tạo bài viết thành công";
    public static final String POST_FILE_CREATED = "Tạo file upload cho bài viết thành công";
    public static final String STORY_CREATED = "Tạo story thành công";
    public static final String STORY_NOT_FOUND = "Không tìm thấy story";
    public static final String STORY_VIEW_CREATED = "Người dùng xem story thành công";
    public static final String POST_UPDATED = "Cập nhật bài viết thành công";
    public static final String POST_DELETED = "Xóa bài viết thành công";
    public static final String REEL_ = "Xóa bài viết thành công";

    // Thông báo chung cho user
    public static final String USER_NOT_FOUND = "Người dùng không tồn tại";
    public static final String USER_CREATED = "Tạo người dùng thành công";
    public static final String USER_UPDATED = "Cập nhập người dùng thanh công";
    public static final String USER_DELETED = "Xoá người dùng thành công";
    public static final String USER_NAME_USED = "Tên người dùng đã tồn tại";
    public static final String USER_NAME_NOT_FORMAT = "Tên người dùng không đúng định dạng";
    public static final String USER_ALREADY_EXISTS = "Người dùng đã tồn tại";
    public static final String PHONE_ALREADY_EXISTS = "Sdt người dùng đã tồn tại";
    public static final String EMAIL_NOT_FORMAT = "Email khônd đúng định dạng";
    public static final String PASSWORD_CHANGED_SUCCESS = "Đổi mật khẩu thành công!";
    public static final String INVALID_FIREBASE_TOKEN = "Mã thông báo Firebase không hợp lệ";

    // Thông báo chung cho follow
    public static final String FOLLOW_CREATE = "Follow thành công";
    public static final String FOLLOW_DELETE = "UnFollow thành công";
    public static final String FOLLOW_FETCH_SUCCESS = "Lấy danh sách Follow thành công";

    // Thông báo lỗi liên quan đến comment
    public static final String LIKE_COMMENT = "Like comment thành công";
    public static final String UNLIKE_COMMENT = "UnLike comment thành công";
    public static final String COMMENT_NOT_FOUND = "Không tìm thấy Commnet";
    public static final String COMMENT_CREATED = "Thêm comment thành công!";
    public static final String UPDATE_COMMENT_SUCCESS = "Sửa Comment thành công!";
    public static final String DELETE_COMMENT = "Xoá comment thành công!";
    public static final String GET_COMMENT_SUCCESS = "Lấy comment thành công thành công!";

    //Thông báo chung cho chat, message, contact
    public static final String CHAT_MESSAGE_SENT_SUCCESS = "Lưu tin nhắn thành công!";
    public static final String CHAT_HISTORY_FETCH_SUCCESS = "Lấy danh sách lịch sử thành công!";
    public static final String CONTACT_CREATED_SUCCESS = "Tạo liên hệ thành công!";
    public static final String CONTACT_DELETED_SUCCESS = "Xoá liên hệ thành công!";

    //Thông báo chung cho đánh giá, like đánh giá
    public static final String REVIEW_NOT_FOUND = "Không tìm thấy đánh giá!";
    public static final String PRODUCT_ALREADY_REVIEWED = "Người dùng đã đánh giá sản phẩm!";
    public static final String LIKE_CREATED = "Like thành công!";
    public static final String UNLIKE_CREATED = "unLike thành công!";
    public static final String REVIEW_DELETED = "Xoá đánh giá thành công!";
    public static final String REVIEW_UPDATED = "Sửa thông tin đánh giá thành công!";
    public static final String COMMENT_SUCCESS = "Lấy danh sách comment thành công!";
    public static final String COMMENT_REPLIES_SUCCESS = "Lấy danh sách comment replies thành công!";
    public static final String REVIEW_CHECKED = "Kiểm tra thành công!";

    //Thông báo chung cho sản phẩm yêu thích
    public static final String WISH_LIST_NOT_FOUND = "Không tìm thấy sản phẩm yêu thích";
    public static final String WISHLIST_FETCH_SUCCESS = "Lấy danh sách yêu thích thành công!";
    public static final String WISHLIST_ADD_SUCCESS = "Thêm vào danh sách yêu thích thành công!";
    public static final String WISHLIST_REMOVE_SUCCESS = "Xoá khỏi danh sách yêu thích thành công!";

    // Thông báo chung khác
    public static final String SYSTEM_ERROR = "Lỗi hệ thống, vui lòng thử lại sau";
    public static final String INVALID_INPUT = "Thông tin đầu vào không hợp lệ";

    // Thông báo cho đăng nhập & xác thưc
    public static final String PASSWORD_RESET_ALREADY_REQUESTED =
            "Bạn đã yêu cầu reset mật khẩu trước đó. Vui lòng kiểm tra email!";
    public static final String PASSWORD_RESET_REQUEST_SUCCESS =
            "Yêu cầu quên mật khẩu đã được gửi thành công!";
    public static final String TOKEN_EXPIRED =
            "Token đã hết hạn!";
    public static final String PASSWORD_RESET_SUCCESS = "Reset mật khẩu thành công!";
    public static final String REFRESH_TOKEN_SUCCESS = "Refresh token thành công";
    public static final String INVALID_CREDENTIALS = "Sai tài khoản hoặc mật khẩu";
    public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
    public static final String OLD_PASSWORD_INCORRECT = "Sai mật khẩu cũ!";

    // Thông báo chung phổ biến
    public static final String DATA_FETCH_SUCCESS = "Lấy dữ liệu thành công!";
    public static final String MISSING_REQUIRED_INFO = "Thiếu thông tin đầu vào!";
    public static final String CONNECT_SERVER_SUCCESS = "Kết nốt đến server thành công!";
    public static final String IMAGE_UPLOAD_SUCCESS = "Tải hình ảnh lên thành công!";
}
