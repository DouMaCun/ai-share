import http from './http'

export const authApi = {
  register: (data: any) => http.post('/auth/register', data),
  login: (data: any) => http.post('/auth/login', data)
}

export const projectApi = {
  list: (params: any) => http.get('/projects', { params }),
  getById: (id: number) => http.get(`/projects/${id}`),
  create: (data: any) => http.post('/projects', data),
  update: (id: number, data: any) => http.put(`/projects/${id}`, data),
  remove: (id: number) => http.delete(`/projects/${id}`),
  mine: () => http.get('/projects/mine'),
  like: (id: number) => http.post(`/projects/${id}/like`),
  favorite: (id: number) => http.post(`/projects/${id}/favorite`)
}

export const tagApi = {
  list: () => http.get('/tags')
}

export const userApi = {
  me: () => http.get('/user/me'),
  updateProfile: (data: any) => http.put('/user/me', data)
}

export const uploadApi = {
  image: (file: File) => {
    const fd = new FormData()
    fd.append('file', file)
    return http.post('/upload/image', fd, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}

export const commentApi = {
  list: (projectId: number) => http.get(`/projects/${projectId}/comments`),
  create: (projectId: number, data: { content: string }) =>
    http.post(`/projects/${projectId}/comments`, data),
  remove: (id: number) => http.delete(`/comments/${id}`)
}
