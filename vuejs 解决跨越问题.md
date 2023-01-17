# vuejs 前端跨越问题解决
1. 在 config/index.js 中的 的dev 添加以下代码，设置一下proxyTable
```js
    proxyTable: {
      '/api': {
        target: 'http://127.0.0.1:8888/wxm', //设置你调用的接口域名和端口号.别忘了加http
        changeOrigin: true, //允许跨域
        // secure: false, // 如果是https接口，需要配置这个参数
        pathRewrite: {
          '^/api': ''
          // ''这里理解成用'/api'代替target里面的地址，后面组件中我们掉接口时直接用api代替。
          // 比如我要调用'http://127.0.0.1:8000/index/'，直接写'/api/index/'即可
        }
      }
    },
```
2. 在 config/dev.env.js 中设置以下代码
```js
module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  API_HOST: "/api/"
})
```
3. 在 config/prod.env.js 中设置以下代码
``` 
module.exports = {
NODE_ENV: ‘“production”’,//生产环境
API_HOST:’“127.0.0.1:8000”’
}
```

4. 测试
> 访问系统地址：http://127.0.0.1:7777
>
> api 接口地址：http://127.0.0.1:8888/wxm
> 
> 调用：
```js
 print () {
            let params = {}
            //必传id，可以在预览页面找到，如图一
            params.excelConfigId = "1347459370216198144"
            //参数对象，在数据源页面配置的参数，接受数组为queryParam，如图二
            params.queryParam = { "username": "admin" }
            //如果有token需要传递token
            let config = {
                headers: { "token": "token" },
            };
            axios.post("api/jmreport/exportPdf", params, config).then(res => {
                console.log('>>>>>', res)
                //成功
                debugger
                if (res.status === 200 && res.data.success) {
                    //获取base64文件
                    let file = res.data.result.file
                    let bstr = atob(file), n = bstr.length, u8arr = new Uint8Array(n);
                    while (n--) {
                        u8arr[n] = bstr.charCodeAt(n);
                    }
                    let blob = new Blob([u8arr], {
                        type: 'application/pdf;chartset=UTF-8'
                    })
                    //创建url
                    let fileURL = URL.createObjectURL(blob)
                    window.open(fileURL)
                }

            })
        }
```

