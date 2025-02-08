"""
屏幕更新调度器
文件遵循 GNU GPLv3 协议，©2025 洛洛希雅 版权所有
"""
import heapq
import time
from collections import deque
from typing import Tuple, Callable


class DisplayScheduler:

    on_send: Callable[[int, Tuple[int, int, int]], None] = lambda x, y: None

    def __init__(self, on_send: Callable[[int, Tuple[int, int, int]], None] = lambda x, y: None):
        # 初始化内存存储，每个指针初始值为(0, 0, 0)
        self.memory = [(0, 0, 0) for _ in range(256)]
        # 记录每个指针最后发送的数据
        self.last_sent = {}
        # 优先队列（最大堆通过负数实现）
        self.priority_queue = []
        # 跟踪已在队列中的指针（避免重复入队）
        self.in_queue = set()
        # 更新历史记录（用于热点统计）
        self.update_history = {i: deque(maxlen=50) for i in range(256)}
        # 低优先级循环的当前索引
        self.low_priority_index = 0
        self.on_send = on_send

    def update_memory(self, pointer: int, data: Tuple[int, int, int]):
        """更新内存数据并触发调度逻辑"""
        if self.memory[pointer] != data:
            self.memory[pointer] = data
            self._add_to_queue(pointer)
            # 记录更新时间（用于热点统计）
            self.update_history[pointer].append(time.time())

    def _add_to_queue(self, pointer: int):
        """将指针添加到优先队列（如果不在队列中）"""
        if pointer not in self.in_queue:
            priority = self._calculate_priority(pointer)
            heapq.heappush(self.priority_queue, (-priority, pointer))
            self.in_queue.add(pointer)

    def _calculate_priority(self, pointer: int) -> float:
        """计算指针的优先级分数"""
        # 基础优先级（热点次数）
        history = self.update_history[pointer]
        hotness = len([t for t in history if t > time.time() - 10])

        # 高频更新检测（3秒内更新超过2次）
        recent_updates = [t for t in history if t > time.time() - 3]
        if len(recent_updates) >= 2:
            return float('inf')  # 最高优先级

        return hotness + 1  # 确保最低优先级为1

    def _should_erase_all(self) -> bool:
        """判断是否需要执行全屏擦除"""
        erase_count = sum(1 for p in range(256)
                          if self.memory[p] == (0, 0, 0) and
                          self.last_sent.get(p, None) != (0, 0, 0))
        return erase_count >= 2

    def run(self):
        """主调度循环"""
        while True:
            # 1. 检查全屏擦除
            if self._should_erase_all():
                self._send_erase_command()
                continue

            # 2. 处理高优先级队列
            if self.priority_queue:
                self._process_priority_queue()
            else:
                self._process_low_priority()

            time.sleep(0.2)  # 遵守发送间隔

    def _send_erase_command(self):
        """执行全屏擦除操作"""
        self.on_send(255, (0x00, 0x00, 0x04))
        # 更新最后发送记录
        for p in range(256):
            if self.memory[p] == (0, 0, 0):
                self.last_sent[p] = (0, 0, 0)
        # 清空队列
        self.priority_queue.clear()
        self.in_queue.clear()

    def _process_priority_queue(self):
        """处理优先队列中的最高优先级任务"""
        _, pointer = heapq.heappop(self.priority_queue)
        self.in_queue.discard(pointer)

        if self.last_sent.get(pointer, None) != self.memory[pointer]:
            self.on_send(pointer, self.memory[pointer])
            self.last_sent[pointer] = self.memory[pointer]

    def _process_low_priority(self):
        """处理低优先级循环更新"""
        start = self.low_priority_index
        for _ in range(256):
            pointer = self.low_priority_index
            self.low_priority_index = (self.low_priority_index + 1) % 256

            if self.last_sent.get(pointer, None) != self.memory[pointer]:
                self.on_send(pointer, self.memory[pointer])
                self.last_sent[pointer] = self.memory[pointer]
                return  # 每次循环只发送一个


if __name__ == "__main__":
    scheduler = DisplayScheduler()


    # 示例：设置高频更新区域（每秒更新一次）
    def clock_updater():
        while True:
            scheduler.update_memory(0, (255, 0x65, 0x87))  # 示例指针0
            time.sleep(1)


    import threading

    threading.Thread(target=clock_updater, daemon=True).start()
    scheduler.run()