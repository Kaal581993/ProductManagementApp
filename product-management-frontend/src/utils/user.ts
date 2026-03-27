export const getDisplayName = (name: string | undefined, isAdmin: boolean) => {
  if (!name) {
    return ''
  }

  if (isAdmin) {
    return name
  }

  return getFirstName(name)
}

export const getFirstName = (name: string | undefined) => {
  if (!name) {
    return ''
  }

  return name.trim().split(/\s+/)[0] ?? name
}
