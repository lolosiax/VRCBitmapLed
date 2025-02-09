/*
 * Copyright (c) 2025 Lolosia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package top.lolosia.vrc.led.plugin;

import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * VRC点阵屏幕插件Java兼容入口点
 *
 * @author 洛洛希雅Lolosia
 * @since 2024-08-14 00:42
 */
public abstract class BitmapLedJavaPlugin extends BitmapLedPlugin {

    @Nullable
    @Override
    public Object onCreate(@NotNull Continuation<? super Unit> $completion) {
        onCreate();
        return Unit.INSTANCE;
    }

    @Nullable
    @Override
    public Object onEnable(@NotNull Continuation<? super Unit> $completion) {
        onEnable();
        return Unit.INSTANCE;
    }

    @Nullable
    @Override
    public Object onUpdate(long nanoTime, @NotNull Continuation<? super Unit> $completion) {
        onUpdate(nanoTime);
        return Unit.INSTANCE;
    }

    @Nullable
    @Override
    public Object onDisable(@NotNull Continuation<? super Unit> $completion) {
        onDisable();
        return Unit.INSTANCE;
    }

    @Nullable
    @Override
    public Object onClose(@NotNull Continuation<? super Unit> $completion) {
        onClose();
        return Unit.INSTANCE;
    }

    @Nullable
    @Override
    public Object onMessage(@NotNull String topic, byte @NotNull [] msg, @NotNull Continuation<? super Unit> $completion) {
        onMessage(topic, msg);
        return Unit.INSTANCE;
    }

    /**
     * 当插件初始化时调用，可以在此方法内初始化一些本地库的运行环境。
     */
    protected void onCreate() {

    }

    /**
     * 当插件启动时调用
     */
    protected void onEnable() {

    }

    /**
     * 以125Hz的速率执行更新任务
     */
    protected void onUpdate(long nanoTime) {

    }

    /**
     * 当插件关闭时调用
     */
    protected void onDisable() {

    }

    /**
     * 当插件被移除时调用。
     * 此时插件应当卸载所有的线程、侦听器、定时任务，并保存数据等待退出。
     */
    protected void onClose() {

    }

    /**
     * 当接收到其他插件传来的消息时调用
     *
     * @param topic 消息类型
     * @param msg   消息内容
     */
    protected void onMessage(String topic, byte[] msg) {

    }

    /**
     * 向其他插件发送消息
     *
     * @param plugin   插件ID
     * @param topic    消息类型
     * @param msg      消息体
     * @param callback 操作结果，若为 true，则发送成功，否则目标插件不存在
     * @throws kotlinx.coroutines.TimeoutCancellationException 目标插件无响应（从callback中抛出）
     */
    void postMessage(String plugin, String topic, byte[] msg, Callback<Boolean> callback) {
        Function<Object, Object> call = (Object o) -> {
            if (o instanceof Result.Failure) {
                callback.failure(((Result.Failure) o).exception);
            } else {
                callback.success((boolean) o);
            }
            return null;
        };
        try {
            Object rs = postMessage(plugin, topic, msg, new Continuation<>() {
                @NotNull
                @Override
                public CoroutineContext getContext() {
                    return BitmapLedJavaPlugin.this.getCoroutineContext();
                }

                @Override
                public void resumeWith(@NotNull Object o) {
                    call.apply(o);
                }
            });
            if (rs != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                call.apply(rs);
            }
        } catch (Throwable e) {
            call.apply(new Result.Failure(e));
        }
    }

    public interface Callback<T> {
        void success(T t);

        default void failure(@NotNull Throwable e) {

        }
    }
}
