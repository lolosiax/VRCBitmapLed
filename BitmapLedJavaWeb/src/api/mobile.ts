import { post } from '@/utils/request'

export interface IServerHost {
  interfaceName: string
  address: string
}

export async function getServerHost(): Promise<IServerHost[]> {
  return post('/mobile/server/host')
}

export async function setMobileText(
  text: string,
  start: number,
  end: number,
  color: string,
  background: boolean
): Promise<void> {
  return post('/mobile/setText', {
    text,
    start,
    end,
    color,
    background
  })
}

export async function switchTimer(): Promise<boolean> {
  return post('/mobile/switchTimer')
}
