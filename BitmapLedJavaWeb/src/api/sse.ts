import type { ApiEventSource } from '@/utils/request'
import { eventSource, post } from '@/utils/request'

export async function sse(): Promise<ApiEventSource> {
  return eventSource('/sse')
}

export async function registrySSE(sseId: string) {
  return post('/sse/registry', { sseId })
}
