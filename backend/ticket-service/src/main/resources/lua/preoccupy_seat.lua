local key = KEYS[1]
local seats = ARGV[1]
local cnt = tonumber(ARGV[2])

local current = redis.call('GET', key)
if not current then
  current = ''
end

if cnt <= 0 then
  return -1
end

redis.call('SET', key, current .. '|pre:' .. seats)
return 1
