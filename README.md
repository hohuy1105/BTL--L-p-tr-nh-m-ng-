<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   TRUYỀN FILE QUA UDP
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

## 📖 1. Giới thiệu
UDP là một trong những giao thức truyền tải chính của bộ giao thức Internet (TCP/IP). Khác với TCP, vốn cần thiết lập kết nối và đảm bảo tính toàn vẹn dữ liệu, UDP hoạt động theo mô hình không kết nối (connectionless). Điều này có nghĩa là khi một ứng dụng gửi dữ liệu, các gói tin (datagram) sẽ được truyền trực tiếp đến địa chỉ đích mà không cần bất kỳ thủ tục bắt tay (handshake) nào.

Đặc điểm quan trọng:

Đơn giản và nhẹ: UDP không duy trì trạng thái phiên làm việc, giúp tiết kiệm tài nguyên hệ thống.
Hiệu năng cao: Tốc độ truyền nhanh do không có bước kiểm tra, xác nhận hay sắp xếp lại gói tin.
Không tin cậy tuyệt đối: Gói tin có thể bị mất, trùng lặp hoặc đến sai thứ tự mà không có cơ chế tự động khắc phục.
Khả năng broadcast/multicast: UDP hỗ trợ gửi dữ liệu đến nhiều thiết bị cùng lúc, phù hợp cho các ứng dụng truyền thông nhóm.
Ứng dụng thực tế:
UDP thường được sử dụng trong các hệ thống cần tốc độ và độ trễ thấp hơn là độ tin cậy tuyệt đối, ví dụ:
Trò chơi trực tuyến (online gaming): truyền thông tin trạng thái nhân vật theo thời gian thực.
Ứng dụng gọi thoại/video (VoIP, video call): truyền âm thanh, hình ảnh với độ trễ thấp.
Streaming (truyền phát video, audio): ưu tiên tốc độ để người dùng xem liên tục.
DNS (Domain Name System): truy vấn tên miền cần phản hồi nhanh, không nhất thiết phải có kết nối lâu dài.

🔹 Cấu trúc gói tin UDP

Một gói tin UDP bao gồm 4 phần chính:

Source Port (16 bit): Cổng nguồn.

Destination Port (16 bit): Cổng đích.

Length (16 bit): Độ dài toàn bộ gói UDP.

Checksum (16 bit): Kiểm tra lỗi cơ bản.

🔹 Ứng dụng của UDP

UDP thường được dùng trong các ứng dụng cần tốc độ hơn độ tin cậy tuyệt đối:

Truyền phát video/audio trực tuyến (YouTube, Zoom, VoIP).

Game online (yêu cầu phản hồi nhanh, chấp nhận mất gói).

DNS (Domain Name System).

Streaming đa phương tiện, IPTV.

## 🔧 2. Ngôn ngữ lập trình sử dụng: [![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)

## 🚀 3. Công nghệ sử dụng

Java: ngôn ngữ lập trình chính.

UDP (User Datagram Protocol): giao thức truyền nhanh, không kết nối.

Socket lập trình mạng: DatagramSocket, DatagramPacket.

Java IO: đọc file (FileInputStream), ghi file (FileOutputStream).

Header Sequence Number: số thứ tự gói tin để ghép file đúng.

## Nguồn
DaiNam University. 
Hồ Quang Huy - 1671020137
Gmail: hoquanghuyhb1@gmail.com


---
