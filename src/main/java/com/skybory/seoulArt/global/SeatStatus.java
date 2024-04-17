package com.skybory.seoulArt.global;

// 좌석 상태 (3가지)
public enum SeatStatus {
    AVAILABLE, // 예약 가능
    RESERVED,  // 예약 완료
    RESERVING  // 예약 중 (다른사람이 예약중일때 표시)
}