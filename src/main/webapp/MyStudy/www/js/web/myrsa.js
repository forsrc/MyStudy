"use strict";
/**
 * var myRsa = MyRsa(n, e, d);
 * var myRsa = MyRsa(n, e, d, true);
 * @param n {String}
 * @param e {Number}
 * @param d {String}
 * @param isNewInstance {boolean} true|false; def: false;
 * @returns {MyRsa|*}
 * @constructor
 */
function MyRsa(n, e, d, isNewInstance) {
    if (!n) {
        throw new Error("Error --> n: is null; e: " + e + "; d: " + d);
    }
    if (!isNewInstance && MyRsa.instance) {
        return MyRsa.instance;
    }
    //this.n = n;
    //this.e = e;
    //this.d = d;
    this.bin = typeof(n) === "string" ? new BigInteger(n) : n;
    this.bie = typeof(e) === "string" ? new BigInteger(e) : e;
    this.bid = typeof(d) === "string" ? new BigInteger(d) : d;
    if (MyRsa._initialized) {
        //return MyRsa.instance;
    }

    /**
     *
     * @param bigInteger {BigInteger}
     * @returns {BigInteger} encrypted bigInteger;
     */
    MyRsa.prototype.encryptBigInteger = function (bigInteger) {
        var __this = this;
        return bigInteger.modPow(__this.bie, __this.bin);
    };

    /**
     * decryptBigInteger(encryptionBigInteger)
     * @param encryptionBigInteger {BigInteger}
     * @returns {BigInteger} decryption bigInteger;
     */
    MyRsa.prototype.decryptBigInteger = function (encryptionBigInteger) {
        var __this = this;
        return encryptionBigInteger.modPow(__this.bid, __this.bin);
    };

    /**
     * getDecryptBigInteger(bigInteger)
     * @param bigInteger {BigInteger|BigIntegerString}
     * @returns {String} decryption bigInteger string;
     */
    MyRsa.prototype.getDecryptBigInteger = function (bigInteger) {
        var __this = this;
        var __bi = typeof bigInteger === "string" ? new BigInteger(bigInteger) : bigInteger;
        var __modPowDN = __bi.modPow(__this.bid, __this.bin);
        return __this.number2string(__modPowDN);
    };

    /**
     * decrypt4Server(bigInteger)
     * @param bigInteger {BigInteger|BigIntegerString}
     * @returns {String} decryption bigInteger string for server;
     */
    MyRsa.prototype.decrypt4Server = function (bigInteger) {
        var __this = this;
        var __bi = typeof bigInteger === "string" ? new BigInteger(bigInteger) : bigInteger;
        var __modPowDN = __bi.modPow(__this.bid, __this.bin);
        return __this.number2string(__modPowDN);
    };

    /**
     * encrypt(string)
     * @param string
     * @returns {String} encrypted string;
     */
    MyRsa.prototype.encrypt = function (string) {
        var __this = this;
        var __msgBigInteger = __this.string2number(string);
        var __encrypt = __this.encryptBigInteger(__msgBigInteger).toString();
        return Base64.encode(__encrypt);
    };

    /**
     * decrypt(base64EncryptedString)
     * @param base64EncryptedString
     * @returns {String} decrypted string;
     */
    MyRsa.prototype.decrypt = function (base64EncryptedString) {
        var __decode = Base64.decode(base64EncryptedString);
        var __encryptedBigInteger = new BigInteger(__decode);
        var __this = this;
        var __decrypt = __this.decryptBigInteger(__encryptedBigInteger);
        return __this.number2string(__decrypt);
    };

    /**
     * string2number(string)
     * @param string
     * @returns {BigInteger} base64 encoded bigInteger;
     */
    MyRsa.prototype.string2number = function (string) {
        var __msg = Base64.encode(string);
        var __numberString = "1";
        var __c = '';
        var __asc = "";
        var __len = __msg.length;
        var i = 0;
        for (; i < __len; ++i) {
            __c = __msg.charAt(i);
            __asc = __c.charCodeAt(0) + "";
            if (__asc.length <= 2) {
                __numberString += "0";
            }
            __numberString += __asc;
        }
        return new BigInteger(__numberString);
    };

    /**
     * number2string(number)
     * @param number {string}
     * @returns {String} base64 decoded string;
     */
    MyRsa.prototype.number2string = function (number) {
        var __numberString = number + "";
        __numberString = __numberString.substring(1, __numberString.length);
        var __message = "";
        var __blockString = "";
        var __block = 0;
        var i = 0;
        for (; i < __numberString.length; i += 3) {
            __blockString = __numberString.substring(i, i + 3);
            __block = parseInt(__blockString);
            __message += String.fromCharCode(__block);
        }
        return Base64.decode(__message.toString());
    };

    MyRsa._initialized = true;
    MyRsa.instance = this;
}