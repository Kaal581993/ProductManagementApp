import { http } from './http'
import type { AddCartItemPayload, Cart, UpdateCartItemPayload } from '../types'

export const getCart = async () => {
  const response = await http.get<Cart>('/api/cart/get-items')
  return response.data
}

export const addCartItem = async (payload: AddCartItemPayload) => {
  const response = await http.post<Cart>('/api/cart/items', payload)
  return response.data
}

export const updateCartItem = async (
  cartItemId: number,
  payload: UpdateCartItemPayload,
) => {
  const response = await http.put<Cart>(`/api/cart/items/${cartItemId}`, payload)
  return response.data
}

export const removeCartItem = async (cartItemId: number) => {
  await http.delete(`/api/cart/items/${cartItemId}`)
}
