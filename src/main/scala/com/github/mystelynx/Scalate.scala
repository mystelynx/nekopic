package com.github.mystelynx

import unfiltered.response.{Html5, ComposeResponse}

/**
 * Created with IntelliJ IDEA.
 * User: urakawa
 * Date: 2012/09/29
 * Time: 8:29
 * To change this template use File | Settings | File Templates.
 */
case class Scalate(uri: String, attributes: (String, Any)*) extends
  ComposeResponse(Html5 { ScalateTemplate.engine.layoutAsNodes("/templates/%s" format uri, attributes.toMap) })

object ScalateTemplate {
  import org.fusesource.scalate._
  val engine = new TemplateEngine
}