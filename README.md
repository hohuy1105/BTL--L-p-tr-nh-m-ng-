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

## 📊 2. Sơ đồ ma trận bãi đỗ xe số hóa (Real-time Matrix)
*Dưới đây là trạng thái giả lập cấu trúc bãi xe thông minh ECO Smart City điều khiển tự động qua dữ liệu mạng IoT:*

| Vị trí ô đỗ | Loại ô đỗ | Trạng thái hiện tại | Phương tiện / Biển số |
| :---: | :---: | :---: | :---: |
| **A-01** | ⚡ Hỗ trợ sạc EV | 🔴 **ĐÃ CÓ XE ĐỖ** | `29A-888.88` (Quét từ AI) |
| **A-02** | 🚗 Ô tô tiêu chuẩn | 🟢 **TRỐNG KHẢ DỤNG** | *Sẵn sàng đón xe...* |
| **A-03** | 🚗 Ô tô tiêu chuẩn | 🟢 **TRỐNG KHẢ DỤNG** | *Sẵn sàng đón xe...* |
| **A-04** | ⚡ Hỗ trợ sạc EV | 🔴 **ĐÃ CÓ XE ĐỖ** | `30F-123.45` (Quét từ AI) |
| **A-05** | 🚗 Ô tô tiêu chuẩn | 🟢 **TRỐNG KHẢ DỤNG** | *Sẵn sàng đón xe...* |

---

## 🔧 3. Công nghệ cốt lõi tích hợp
* **Python Backend:** Hệ thống FastAPI máy chủ trung tâm điều phối, mở cổng kết nối thông suốt mạng mạng `port 8000`.
* **AI Computer Vision:** Thư viện OpenCV quản lý Webcam chụp ảnh thời gian thực phối hợp mô hình **EasyOCR** bóc tách biển số xe.
* **Firmware Hardware:** Ngôn ngữ C++ biên dịch nhúng qua Arduino IDE nạp chip xử lý vô tuyến không dây **SoC Wi-Fi ESP32**.
* **Actuators:** Động cơ Servo điều khiển thanh chắn Barrier tự động nâng góc 90° và cụm cảm biến khoảng cách phản xạ.
* **Web UI Center:** Giao diện điều khiển IOC Dashboard tương tác thời gian thực tích hợp phân hệ **Chatbot Trợ lý ảo AI**.

---

## 📝 4. Cấu trúc thư mục mã nguồn nguồn dự án
```text
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
👨‍💻 5. Thông tin tác giả đề tài
Đơn vị công tác: Trường Đại học Đại Nam (DaiNam University) - Khoa Công nghệ Thông tin.
Sinh viên thực hiện: Hồ Quang Huy
Mã số sinh viên (MSSV): 1671020137
Phòng nghiên cứu: AIoTLab Đô thị thông minh.
Email liên hệ: hoquanghuyhb1@gmail.com
