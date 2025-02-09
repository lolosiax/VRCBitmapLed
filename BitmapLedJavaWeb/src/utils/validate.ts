/**
 * @description 判读是否为外链
 * @param path
 * @returns {boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @description 校验密码是否小于6位
 * @param str
 * @returns {boolean}
 */
export function isPassword(str) {
  return str.length >= 6
}

/**
 * @description 判断是否为数字
 * @param value
 * @returns {boolean}
 */
export function isNumber(value) {
  const reg = /^[0-9]*$/
  return reg.test(value)
}

/**
 * @description 判断是否是名称
 * @param value
 * @returns {boolean}
 */
export function isName(value) {
  const reg = /^[\u4E00-\u9FA5a-zA-Z0-9]+$/
  return reg.test(value)
}

/**
 * @description 判断是否为IP
 * @param ip
 * @returns {boolean}
 */
export function isIP(ip) {
  const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
  return reg.test(ip)
}

/**
 * @description 判断是否是传统网站
 * @param url
 * @returns {boolean}
 */
export function isUrl(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @description 判断是否是小写字母
 * @param str
 * @returns {boolean}
 */
export function isLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @description 判断是否是大写字母
 * @param str
 * @returns {boolean}
 */
export function isUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @description 判断是否是大写字母开头
 * @param str
 * @returns {boolean}
 */
export function isAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @description 判断是否是字符串
 * @param str
 * @returns {boolean}
 */
export function isString(str) {
  return typeof str === 'string' || str instanceof String
}

/**
 * @description 判断是否是数组
 * @param arg
 * @returns {arg is any[]|boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

/**
 * @description 判断是否是数组
 * @param arg
 * @returns {arg is any[]|boolean}
 */
export function isObject(arg) {
  return Object.prototype.toString.call(arg) === '[object Object]'
}

/**
 * @description 判断是否是端口号
 * @param str
 * @returns {boolean}
 */
export function isPort(str) {
  const reg = /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/
  return reg.test(str)
}

/**
 * @description 判断是否是手机号
 * @param str
 * @returns {boolean}
 */
export function isPhone(str) {
  const reg = /^1\d{10}$/
  return reg.test(str)
}

/**
 * @description 判断是否是身份证号(第二代)
 * @param str
 * @returns {boolean}
 */
export function isIdCard(str) {
  const reg = /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
  return reg.test(str)
}

/**
 * @description 判断是否是邮箱
 * @param str
 * @returns {boolean}
 */
export function isEmail(str) {
  const reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
  return reg.test(str)
}

/**
 * @description 判断是否中文
 * @param str
 * @returns {boolean}
 */
export function isChina(str) {
  const reg = /^[\u4E00-\u9FA5]{2,4}$/
  return reg.test(str)
}

/**
 * @description 判断是否为空
 * @param str
 * @returns {boolean}
 */
export function isBlank(str) {
  return str == null || false || str === '' || str.trim() === '' || str.toLocaleLowerCase().trim() === 'null'
}

/**
 * @description 判断是否为固话
 * @param str
 * @returns {boolean}
 */
export function isTel(str) {
  const reg = /^(400|800)([0-9\\-]{7,10})|(([0-9]{4}|[0-9]{3})(-| )?)?([0-9]{7,8})((-| |转)*([0-9]{1,4}))?$/
  return reg.test(str)
}

/**
 * @description 判断是否为数字且最多两位小数
 * @param str
 * @returns {boolean}
 */
export function isNum(str) {
  const reg = /^\d+(\.\d{1,2})?$/
  return reg.test(str)
}
/**
 * 检验18位身份证号码
 * @param cid 18为的身份证号码
 * @return Boolean 是否有效
 **/
export function isIdentityNumber(cid: string) {
  const arrExp = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2] //加权因子
  const arrValid = '1 0 X 9 8 7 6 5 4 3 2'.split(' ') //校检码
  if (/^\d{17}\d|x$/i.test(cid)) {
    let sum = 0
    for (let i = 0; i < cid.length - 1; i++) {
      // 对前17位数字与权值乘积求和
      sum += Number.parseInt(cid.slice(i, i + 1), 10) * arrExp[i]
    }
    // 计算模（固定算法）
    const idx = sum % 11
    // 检验第18为是否与校验码相等
    return arrValid[idx] === cid.slice(17, 18).toUpperCase()
  } else {
    return false
  }
}
/**
 * EL表单检测
 * @author 一七年夏
 */
export class ElValidate {
  /**
   * 创建一个空值检查 Validate
   * @param canEmpty {boolean | string} 是否可空，或非空提示文本
   * @param validate {[any]} Validate列表
   * @return {[any]}
   */
  static createValidate(canEmpty, validate) {
    if (isString(canEmpty) || (typeof canEmpty == 'boolean' && canEmpty)) {
      validate.forEach((v) => {
        v.required = undefined
        const va = v.validator
        v.validator = function (a, b, c) {
          if (!b) c()
          else va(a, b, c)
        }
      })
      return validate
    } else {
      validate.push({
        required: true,
        trigger: 'blur',
        message: isString(canEmpty) ? canEmpty : '请输入文本'
      })
      return validate
    }
  }

  /**
   * 获取是否是手机号的表单验证对象数组
   * @return {[any]}
   */
  static getIsPhoneRule(canEmpty = false) {
    return this.createValidate(canEmpty ? '请输入手机号' : false, [
      {
        trigger: 'blur',
        message: '请输入有效的手机号',
        validator(rule, value, callback) {
          if (/^1[3456789]\d{9}$/g.test(value)) callback()
          else callback(new Error('无效的手机号'))
        }
      }
    ])
  }

  /**
   * 获取是否是身份证号的表单验证数组
   * @return {[any]}
   */
  static getIsIdentityNumberRule(canEmpty = false) {
    return this.createValidate(canEmpty ? '请输入身份证号' : false, [
      {
        trigger: 'blur',
        message: '请输入有效的身份证号',
        validator(rule, value, callback) {
          if (isIdentityNumber(value)) {
            callback()
          } else {
            callback(new Error('无效的身份证号'))
          }
        }
      }
    ])
  }

  /**
   * 获取限制文本长度的的表单验证数组
   * @param canEmpty 是否允许空文本
   * @param max 最大长度，“-1”为无限制
   * @param min 最小长度，默认为“0”
   * @return {[any]}
   */
  static getLengthLimitRule(canEmpty = false, max = -1, min = 0) {
    let lint = `文本长度应在${min}到${max}之间`
    if (min <= 0) lint = `最大输入${max}个字`
    else if (max >= -1) lint = `最少输入${min}个字`
    return this.createValidate(canEmpty ? '请输入文本' : false, [
      {
        trigger: 'change',
        message: lint,
        validator(rule, value, callback) {
          if (value.length < min) {
            callback(new Error(`输入文本太短，最少长度为${min}`))
            return
          }
          if (max > -1 && value.length > max) {
            callback(`输入文本太长，最长长度为${max}`)
            return
          }
          callback()
        }
      }
    ])
  }

  /**
   * 检查是否为空字符串的表单验证数组
   * @param str 检查失败时显示的文本
   * @return {[any]}
   */
  static getIsEmptyRule(str = '请输入文本') {
    return [{ required: true, trigger: 'blur', message: str }]
  }
}
