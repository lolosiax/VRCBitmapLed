//bus even
import { registrySSE, sse } from '@/api/sse'
import { delay } from '@/utils/common-util'
import type { ApiEventSource } from '@/utils/request'
import mitt from 'mitt'

const instance = mitt()
const listeners: string[] = []
let source: ApiEventSource | null = null

type Mitt = typeof instance
type EventBus = Omit<Mitt, "on" | "off"> & {
  on<T>(key: string, callback: (it: T) => void): void
  off(key: string, callback?: any)
}

/**
 * 创建一个Proxy，拦截on方法
 * @param mitt Mitt
 */
function createProxy(mitt: Mitt): Mitt {
  return new Proxy<Mitt>(mitt, {
    get(target: any, p: string | symbol) {
      if (p !== 'on') return target[p]
      // 拦截 on 方法，并将消息转发到 mitt 中
      return new Proxy(target.on, {
        apply(_: any, _1: any, argArray: any[]) {
          if (typeof argArray[0] === 'string' && !listeners.includes(argArray[0])) {
            listeners.push(argArray[0])
            registryListener(argArray[0])
          }
          return (target as any).on(...argArray)
        }
      })
    }
  }) as Mitt
}

function registryListener(name: string) {
  source?.addEventListener(name, (e) => {
    instance.emit(name, JSON.parse(e.data))
  })
}

const export1 = createProxy(instance)

export default export1
export function useEventBus() : EventBus {
  return export1
}
export const getSSEConnect = () => source

export async function initSSEConnect() {
  while (true) {
    try {
      source = await sse()
      if (source != null) {
        try {
          await registrySSE(source.id)
        }
        catch {
          source.close()
        }
      }
      listeners.forEach(registryListener)
      await new Promise<void>((r) => {
        source!.addEventListener('close', () => {
          r()
        })
        source!.addEventListener('error', (e) => {
          console.error(e)
        })
      })
    } catch (e: any) {
      console.error(e)
    }
    await delay(5000)
  }
}
