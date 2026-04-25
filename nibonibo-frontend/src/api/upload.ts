import request from './request'

function uploadFile(url: string, file: File, onProgress?: (percentage: number) => void) {
  const formData = new FormData()
  formData.append('file', file)

  return request.post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    onUploadProgress: (event) => {
      if (!event.total || !onProgress) {
        return
      }

      onProgress(Math.round((event.loaded / event.total) * 100))
    },
  }) as Promise<string>
}

export function uploadImage(file: File, onProgress?: (percentage: number) => void) {
  return uploadFile('/upload/image', file, onProgress)
}

export function uploadVideo(file: File, onProgress?: (percentage: number) => void) {
  return uploadFile('/upload/video', file, onProgress)
}
