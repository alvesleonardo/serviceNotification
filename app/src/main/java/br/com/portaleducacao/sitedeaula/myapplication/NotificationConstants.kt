package br.com.portaleducacao.sitedeaula.myapplication

/**
 * Created by bk_leonardo.alves on 10/01/2018.
 */
class NotificationConstants{
    interface SERVICES{
        companion object {
            var UPLOAD_FOREGROUND_SERVICE = 101
            val CHANNEL_ID = "UPLOAD_CHANNEL"
            val CHANNEL_NAME = "Vídeos em upload em background"
            val CHANNEL_DESCRIPTION = "Vídeos subindo para seu repositório pessoal"
        }
    }
}