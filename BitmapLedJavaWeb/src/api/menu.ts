import { post } from '@/utils/request'

export async function getMenuConfig(): Promise<{ [P: string]: boolean }> {
  return post('/menu/config')
}
