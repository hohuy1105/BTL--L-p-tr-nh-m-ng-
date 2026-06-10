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
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="FIT DNU Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>
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
