# 🏠 [FE-HPT-StayEasy-QuanLyNhaTro]
**Hệ thống quản lý nhà trọ StayEasy**

---

## 🧾 Mô tả dự án
Dự án nhằm xây dựng hệ thống hỗ trợ chủ nhà trọ trong việc:
- Quản lý hợp đồng & phiên bản hợp đồng
- Quản lý người thuê
- Quản lý phòng, tình trạng phòng
- Theo dõi thanh toán
- Báo cáo doanh thu và lịch sử thuê

---

## 🚀 Quy tắc làm việc với Git

### 🔀 Quy ước branch
| Mục đích                  | Tên nhánh ví dụ                  |
|---------------------------|----------------------------------|
| Mã nguồn chính             | `main`                           |
| Phát triển tổng hợp       | `dev`                            |
| Tính năng mới             | `feature/add-contract-version`   |
| Sửa lỗi                   | `bugfix/fix-contract-end-date`   |
| Cá nhân (nếu cần)         | `dev/hoang-room-crud`            |

### ⚠️ Lưu ý:
- **Không được push trực tiếp code chưa kiểm tra lên `main`**
- Code đang dev chỉ được đẩy lên `dev` hoặc nhánh feature riêng
- Kiểm tra branch hiện tại:
  ```bash
  git branch
