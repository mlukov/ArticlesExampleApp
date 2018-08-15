package com.mlukov.articles.domain.repositories.local

import android.util.Log
import com.google.gson.Gson
import com.mlukov.articles.domain.models.ArticleDataList
import io.reactivex.Single
import java.io.*
import java.lang.reflect.Type
import java.nio.charset.Charset
import javax.inject.Inject
import kotlin.jvm.java

class LocalStorageRepository @Inject
constructor(private val cacheFolder : File, private val gson : Gson) : ILocalStorageRepository {

    companion object {

        private val TAG = LocalStorageRepository::class.simpleName
        private val articlesCacheFile = TAG + "_articles.json"
    }

    override fun dropArticleDataListCache() {

        try {
            deleteFile( articlesCacheFile)
        }
        catch ( ex: Exception ){
            Log.e( TAG, ex.message, ex)
        }
    }

    override fun getArticleDataListFromCache() : Single<ArticleDataList> {

        return Single.create<ArticleDataList> { emitter ->
            var repositories : ArticleDataList? = null

            try {

                repositories = restoreObjectFromFile<ArticleDataList>(ArticleDataList::class.java, articlesCacheFile)

            } catch (ex : Exception) {

                ex.printStackTrace()
            }

            if (!emitter.isDisposed()) {

                if (repositories == null) {
                    emitter.onSuccess(ArticleDataList.empty())
                } else {
                    emitter.onSuccess(repositories!!)
                }
            }
        }
    }

    override fun addArticleDataListToCache(articleDataList : ArticleDataList) : Single<ArticleDataList> {

        return Single.fromCallable( {
            try {
                saveObjectToFile( articleDataList, ArticleDataList::class.java, articlesCacheFile)
            }
            catch ( ex: Exception){
                Log.e( TAG, ex.message, ex )
            }

            return@fromCallable articleDataList
        })
    }

    //region private methods
    @Throws(IOException::class)
    private fun saveObjectToFile(src : Any, typeOfSrc : Type, fileName : String) {

        val jsonString = gson.toJson(src, typeOfSrc)
        overwriteTextFile(fileName, jsonString)
    }

    @Throws(IOException::class)
    private fun <T> restoreObjectFromFile(typeOfSrc : Type, fileName : String) : T? {

        val jsonString = readTextFile(fileName)
        return gson.fromJson<T>(jsonString, typeOfSrc)
    }

    @Throws(IOException::class)
    private fun createNewOrOverwrite(folderName : String, fileName : String) : File {

        val file = File(folderName, fileName)

        if (file.exists())
            file.delete()

        file.createNewFile()
        return file

    }

    private fun deleteFile(fileName : String) {

        val file = File(cacheFolder.getAbsolutePath(), fileName)

        if (file.exists())
            file.delete()
    }

    @Throws(IOException::class)
    private fun overwriteTextFile(fileName : String, textToWrite : String) {

        var output : Writer? = null

        try {
            val file = createNewOrOverwrite(cacheFolder.getAbsolutePath(), fileName)
            output = BufferedWriter(FileWriter(file))
            output.write(textToWrite)
        } finally {

            if (output != null) {
                output.close()
            }
        }
    }

    @Throws(IOException::class)
    private fun readTextFile(fileName : String) : String? {

        var inputStream : InputStream? = null
        var textContent : String? = null

        try {

            val file = File(cacheFolder.getAbsolutePath(), fileName)

            if (file.exists()) {

                inputStream = FileInputStream(file)
                val size = inputStream!!.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                textContent = String(buffer, Charset.forName("UTF-8"))
            }
        } finally {

            if (inputStream != null) {
                inputStream.close()
            }
        }

        return textContent
    }
}