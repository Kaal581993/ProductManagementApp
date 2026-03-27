import { http } from './http'
import type {
  Product,
  ProductCreatePayload,
  ProductUpdatePayload,
} from '../types'

export const getProducts = async () => {
  const response = await http.get<Product[]>('/api/products')
  return response.data
}

export const createProduct = async (payload: ProductCreatePayload) => {
  const response = await http.post<Product>('/api/products', payload)
  return response.data
}

export const updateProduct = async (id: number, payload: ProductUpdatePayload) => {
  const response = await http.put<Product>(`/api/products/${id}`, payload)
  return response.data
}

export const updateProductStatus = async (id: number, enabled: boolean) => {
  const response = await http.patch<Product>(`/api/products/${id}/status`, {
    enabled,
  })
  return response.data
}
