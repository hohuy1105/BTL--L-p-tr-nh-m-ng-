<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   HỆ THỐNG QUẢN LÝ BÃI ĐỖ XE THÔNG MINH TRUNG TÂM (IOC CENTER)
</h2>
<div align="center">
    <p align="center">
        <img src="https://raw.githubusercontent.com/formdev/flatlaf/main/flatlaf-theme-editor/src/main/resources/com/formdev/flatlaf/themeeditor/icons/project.png" alt="AIoTLab Logo" width="80"/>
        <img src="https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge" alt="FIT DNU"/>
        <img src="https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge" alt="DNU"/>
    </p>
</div>

---

## 📖 1. Giới thiệu dự án
Trong kỷ nguyên phát triển đô thị thông minh (Smart City), việc tối ưu hóa hạ tầng giao thông tĩnh bằng Trí tuệ nhân tạo (AI) và Internet vạn vật (IoT) là xu hướng tất yếu. Dự án **"Smart Parking AI Center"** ra đời nhằm số hóa các ô đỗ vật lý thông qua mạch vi điều khiển kết nối không dây Wi-Fi ESP32, tự động chụp ảnh và giải mã biển số phương tiện bằng mô hình học sâu AI OCR tại máy chủ trung tâm Python FastAPI.

---

## 🔧 2. Công nghệ sử dụng
* **Python Backend:** Hệ thống FastAPI máy chủ trung tâm điều phối, mở cổng kết nối thông suốt mạng mạng `port 8000`.
* **AI Computer Vision:** Thư viện OpenCV quản lý Webcam chụp ảnh thời gian thực phối hợp mô hình **EasyOCR** bóc tách biển số xe.
* **Firmware Hardware:** Ngôn ngữ C++ biên dịch nhúng nạp chip xử lý vô tuyến không dây **SoC Wi-Fi ESP32**.
* **Web UI Center:** Giao diện điều khiển IOC Dashboard tương tác thời gian thực tích hợp phân hệ **Chatbot Trợ lý ảo AI**.

---

## 🚀 3. Hình ảnh chức năng hệ thống (System Interface)

<div align="center">
  
### 🟢 TRẠNG THÁI 1: KHỞI ĐỘNG BÃI XE TRỐNG HOÀN TOÀN (100% AVAILABLE)
*Khi vừa khởi chạy máy chủ Python trung tâm, toàn bộ ma trận 5 ô đỗ hiển thị trạng thái TRỐNG màu xanh lá cây vô cùng trực quan.*

```text
┌───────────────────────────────────────────────────────────────────────────┐
│ [LIVE CAMERA]                     │ [TRỢ LÝ SỐ PHỤC VỤ CÔNG DÂN AI]       │
│                                   │                                       │
│      📷 (Webcam Active)           │ Trợ lý xanh: Xin chào! Tôi là AI      │
│      Luồng video trực tiếp        │ phản hồi hệ thống Green Parking.      │
│                                   │                                       │
├───────────────────────────────────┴───────────────────────────────────────┤
│ > [SYSTEM ACTIVE]: Mạch ESP32 báo ô đỗ số 1 TRỐNG. Đang túc trực mạng... │
├───────────────────────────────────────────────────────────────────────────┤
│  [ 🟢 A-01 ]   [ 🟢 A-02 ]   [ 🟢 A-03 ]   [ 🟢 A-04 ]   [ 🟢 A-05 ]  │
│     TRỐNG         TRỐNG         TRỐNG         TRỐNG         TRỐNG         │
└───────────────────────────────────────────────────────────────────────────┘
🔴 TRẠNG THÁI 2: XE VẬT LÝ VÀO Ô ĐỖ - KÍCH HOẠT CAMERA QUÉT AI OCR
Mạch phần cứng ESP32 phát hiện vật cản che mắt cảm biến -> Gọi API ra lệnh chụp ảnh thật và nhuộm đỏ ô xe tự động.
Plaintext
┌───────────────────────────────────────────────────────────────────────────┐
│ [LIVE CAMERA]                     │ [TRỢ LÝ SỐ PHỤC VỤ CÔNG DÂN AI]       │
│                                   │                                       │
│      🚗 [Ảnh phương tiện]         │ Công dân: Xe 29A88888 đỗ ở đâu?       │
│      Quét vùng biển số...         │                                       │
│      Detected: 29A-888.88         │ Trợ lý xanh: 📍 Định vị thành công!  │
│                                   │ Xe đang đỗ an toàn tại ô số [A-01].   │
├───────────────────────────────────┴───────────────────────────────────────┤
│ > [ALERT IoT]: Ô đỗ số 1 có xe vào! Biển số AI: 29A-888.88. Mở Barrier!  │
├───────────────────────────────────────────────────────────────────────────┤
│  [ 🔴 A-01 ]   [ 🟢 A-02 ]   [ 🟢 A-03 ]   [ 🟢 A-04 ]   [ 🟢 A-05 ]  │
│  29A-888.88       TRỐNG         TRỐNG         TRỐNG         TRỐNG         │
└───────────────────────────────────────────────────────────────────────────┘
📊 4. Sơ đồ ma trận bãi đỗ xe số hóa (Real-time Matrix Table)
Vị trí ô đỗ	Loại ô đỗ	Trạng thái hiện tại	Phương tiện / Biển số
A-01	⚡ Hỗ trợ sạc EV	🔴 ĐÃ CÓ XE ĐỖ	29A-888.88 (Quét từ AI)
A-02	🚗 Ô tô tiêu chuẩn	🟢 TRỐNG KHẢ DỤNG	Sẵn sàng đón xe...
A-03	🚗 Ô tô tiêu chuẩn	🟢 TRỐNG KHẢ DỤNG	Sẵn sàng đón xe...
A-04	⚡ Hỗ trợ sạc EV	🔴 ĐÃ CÓ XE ĐỖ	30F-123.45 (Quét từ AI)
A-05	🚗 Ô tô tiêu chuẩn	🟢 TRỐNG KHẢ DỤNG	Sẵn sàng đón xe...
📝 5. Cấu trúc thư mục mã nguồn dự án
Plaintext
smart-parking-ai-center/
│
├── backend/
│   ├── main.py                 # File lõi máy chủ Python FastAPI & Lõi học sâu AI EasyOCR
│   └── requirements.txt        # Danh sách thư viện môi trường máy chủ Python
│
├── frontend/
│   ├── index.html              # Giao diện Web Trung tâm điều hành IOC Dashboard công nghệ
│   └── app.js                  # Logic kết nối máy ảnh, chatbot dữ liệu và vòng lặp Polling
│
└── hardware/
    └── plant-monitoring-system.ino  # Mã nguồn nhúng C++ nạp vĩnh viễn vào mạch ESP32
👨‍💻 6. Thông tin tác giả đề tài
Đơn vị công tác: Trường Đại học Đại Nam (DaiNam University) - Khoa Công nghệ Thông tin.
Sinh viên thực hiện: Hồ Quang Huy
Mã số sinh viên (MSSV): 1671020137
Phòng nghiên cứu: AIoTLab Đô thị thông minh.
Email liên hệ: hoquanghuyhb1@gmail.com
### 2. Cài đặt Máy chủ Trí tuệ nhân tạo Python Backend
* Huy mở khung Terminal nằm ngay trên ứng dụng phần mềm biên dịch VS Code và di chuyển thư mục, gõ lệnh cài đặt gói môi trường thư viện:
  ```bash
  cd backend
  pip install -r requirements.txt
Hệ thống sẽ tự động cấu hình và tải toàn bộ các gói phần mềm tối ưu: fastapi, uvicorn, easyocr, opencv-python.
3. Đồng bộ hóa liên kết Giao diện Web Frontend
Mở thư mục frontend/, đảm bảo tệp script logic liên kết điều khiển camera và chatbot dữ liệu mạng app.js đã nằm ngang hàng đồng bộ với file cấu trúc giao diện index.html.
Kiểm tra dòng số 1 của file app.js đã trỏ đúng cổng local của máy tính: const SERVER_URL = "http://localhost:8000";.
Hướng dẫn sử dụng
Hệ thống vận hành đồng bộ nhịp nhàng thông qua chuỗi 4 bước thao tác theo quy trình thực tế dưới đây:
Bước 1: Khởi động Máy chủ Trung tâm điều hành Python
Tại Terminal của thư mục dự án backend/, Huy gõ câu lệnh khởi động server chạy dải host mạng đa năng:
Bash
python3 main.py
Bạn hãy giữ nguyên màn hình cửa sổ Terminal này chạy ngầm không tắt. Khi hệ thống báo dòng trạng thái chữ xanh: INFO: Uvicorn running on http://0.0.0.0:8000 nghĩa là máy chủ AI đã sẵn sàng kết nối.
Bước 2: Bật màn hình Trung tâm điều hành Web Dashboard IOC
Huy nhấp chuột phải trực tiếp vào file frontend/index.html bên cột Explorer chọn Open with Live Server. Trình duyệt Chrome sẽ tự động bật mở giao diện đồ thị bãi đỗ xe thông minh.
Mẹo mở khóa Camera bảo mật trên Chrome: Trên thanh địa chỉ của trình duyệt Chrome hiện ra, Huy click chuột xóa số tĩnh 127.0.0.1 đi và sửa lại viết chữ thành chữ localhost (Đường dẫn dạng: http://localhost:5500/frontend/index.html) rồi gõ Enter. Trình duyệt hiện hộp thoại xin quyền máy ảnh, Huy bấm chọn nút Cho phép (Allow).
Thành quả rực rỡ: Đèn camera Webcam trên máy Mac tự động sáng và hình ảnh luồng video trực tiếp của bạn hiển thị căng nét ngay trên giao diện web, dòng chữ báo lỗi đỏ biến mất hoàn toàn!
Bước 3: Cấp nguồn thiết bị phần cứng thực địa
Huy cắm cáp cấp điện nguồn cho bo mạch ESP32 (Thông qua củ sạc hoặc sạc dự phòng). Mạch tự động nháy đèn kết nối vô tuyến vào mạng Wi-Fi lớp học.
Lúc này quan sát trên giao diện Web, cả 5 ô đỗ xe từ A-01 đến A-05 đều hiển thị đồng loạt màu Xanh dương (Trạng thái TRỐNG).
Bước 4: Demo kịch bản quy trình chạy thực tế Đồ án trước hội đồng
Xe tiến vào: Huy dùng một chiếc mô hình ô tô hoặc bàn tay che mắt cảm biến khoảng cách nối chân 32 của mạch ESP32. Mạch lập tức bắn gói tin mạng không dây HTTP truyền JSON lên máy Mac.
AI Kích hoạt: Máy chủ Python tự gọi camera chụp hình thật một ảnh, nạp vào lõi AI học sâu giải mã biển số xe (Ví dụ: 29A-888.88). Thanh chắn Servo cổng chân 22 tự động gạt góc 90 
∘
  mở Barrier. Đồng thời, ô đỗ A-01 trên Web Chrome tự động đổi màu Đỏ rực rỡ hiện biển xe mà không cần tải lại trang.
Hỏi đáp Trợ lý ảo Chatbot: Huy sang phân hệ ô cửa sổ bên phải gõ câu hỏi: "xe 29A88888 đỗ ở đâu?" và nhấn gửi. Trợ lý số thông minh tự động so khớp cơ sở dữ liệu thời gian thực và chat phản hồi định vị chuẩn xác: "📍 Định vị thành công! Xe của bạn đang đỗ an toàn tại vị trí ô số [A-01]". Gõ tiếp "giá vé bao nhiêu" -> AI phản hồi ngay lập tức bảng giá cước đô thị xanh.
👨‍💻 Thông tin nguồn Tác giả Đề tài
Học viện đào tạo: Trường Đại học Đại Nam (DaiNam University) - Khoa Công nghệ Thông tin.
Nghiên cứu sinh thực hiện: Hồ Quang Huy
Mã số sinh viên (MSSV): 1671020137
Phòng nghiên cứu ứng dụng: AIoTLab Đô thị thông minh.
Hòm thư liên hệ chính thức: hoquanghuyhb1@gmail.com
