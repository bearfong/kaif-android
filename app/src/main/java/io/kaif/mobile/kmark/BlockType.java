/*
 * Copyright (C) 2015 Koji Lin <koji.lin@gmail.com>
 * Copyright (C) 2011 René Jeschke <rene_jeschke@yahoo.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.kaif.mobile.kmark;

/**
 * Block type enum.
 *
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
enum BlockType {
  /**
   * Unspecified. Used for root block and list items without paragraphs.
   */
  NONE,
  /**
   * A fenced code block.
   */
  FENCED_CODE,
  /**
   * A ordered list item.
   */
  ORDERED_LIST_ITEM,
  /**
   * A unordered list item.
   */
  UNORDERED_LIST_ITEM,
  /**
   * An ordered list.
   */
  ORDERED_LIST,
  /**
   * A paragraph.
   */
  PARAGRAPH,
  /**
   * An unordered list.
   */
  UNORDERED_LIST,

  /**
   * Block Quote
   */
  BLOCKQUOTE,

}
